package com.ninjas.movietime.service;

import com.codahale.metrics.Timer;
import com.google.common.base.Optional;
import com.ninjas.movietime.core.domain.exception.CannotFindIMDIdException;
import com.ninjas.movietime.core.domain.exception.CannotFindRottenTomatoesRatingException;
import com.ninjas.movietime.core.domain.exception.CannotFindTrackTvInformationException;
import com.ninjas.movietime.core.domain.movie.Movie;
import com.ninjas.movietime.core.domain.showtime.Showtime;
import com.ninjas.movietime.core.domain.theater.Theater;
import com.ninjas.movietime.core.domain.theater.TheaterChain;
import com.ninjas.movietime.core.util.DateUtils;
import com.ninjas.movietime.core.util.ExceptionManager;
import com.ninjas.movietime.core.util.MetricManager;
import com.ninjas.movietime.integration.AlloCineAPI;
import com.ninjas.movietime.integration.ImdbAPI;
import com.ninjas.movietime.integration.RottenTomatoesAPI;
import com.ninjas.movietime.integration.TraktTvAPI;
import com.ninjas.movietime.repository.IntegrationRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author ayassinov on 27/08/2014.
 */
@Slf4j
@Service
public class IntegrationService {

    private final String className = this.getClass().getCanonicalName();

    private final IntegrationRepository integrationRepository;
    private final AlloCineAPI alloCineAPI;
    private final ImdbAPI imdbAPI;
    private final RottenTomatoesAPI rottenTomatoesAPI;
    private final TraktTvAPI traktTvAPI;

    @Autowired
    public IntegrationService(IntegrationRepository integrationRepository,
                              AlloCineAPI alloCineAPI,
                              ImdbAPI imdbAPI,
                              RottenTomatoesAPI rottenTomatoesAPI,
                              TraktTvAPI traktTvAPI) {
        this.integrationRepository = integrationRepository;
        this.alloCineAPI = alloCineAPI;
        this.imdbAPI = imdbAPI;
        this.rottenTomatoesAPI = rottenTomatoesAPI;
        this.traktTvAPI = traktTvAPI;
    }

    /**
     * Add new/Update Theater and TheaterChain from AlloCineAPI
     */
    public boolean updateTheaters() {

        final Optional<Timer.Context> timer = MetricManager.startTimer(className, "updateTheaters");
        try {
            final List<Theater> allByRegion = alloCineAPI.findAllInParis();
            integrationRepository.saveTheater(allByRegion);
            log.debug("List of all theaters in paris {} updated", allByRegion.size());
            return true;
        } catch (Exception ex) {
            ExceptionManager.log(ex, "Exception On Updating Movie Theaters");
            return false;
        } finally {
            MetricManager.stopTimer(timer);
        }
    }

    /**
     * Update showtime for all open Theater, that are tracked or not by Movie Time
     * from AlloCine, TheMovieDb.org, RottenTomatoes, TrackTV
     *
     * @param isTrackedOnly if true the api will restrict the showtime
     *                      to only those how are official see TheaterChain class for the complete list
     */
    public boolean updateMovieShowtime(boolean isTrackedOnly) {
        updateShowtime(isTrackedOnly);
        updateImdbId();
        updateTraktTvInformation();
        updateRottenTomatoesInformation();
        return true;
    }


    /**
     * Update showtime and movie information from alloCine API
     *
     * @param isTrackedOnly if true restrict the showtime only to the theater that we are tracking.
     */
    private void updateShowtime(boolean isTrackedOnly) {
        final Optional<Timer.Context> timer = MetricManager.startTimer(className, "updateShowtime");
        try {
            //find theater Chain
            final List<TheaterChain> theaterChains = integrationRepository.listAllTheaterChain(isTrackedOnly);
            //iterate over every chain to get the theaters list
            for (final TheaterChain theaterChain : theaterChains) {
                //list all theaters, returning only id field
                final List<Theater> theaters = integrationRepository.listOpenTheaterByTheaterChain(theaterChain, false);
                //find showtime from Allo cine API
                final List<Showtime> showtimes = alloCineAPI.findShowtime(theaters);
                for (Showtime showtime : showtimes) {
                    integrationRepository.saveShowtime(showtime);
                }
            }
        } finally {
            MetricManager.stopTimer(timer);
        }
    }

    /**
     * update imdb code and rating for newly added Movie
     */
    private void updateImdbId() {
        final Optional<Timer.Context> timer = MetricManager.startTimer(className, "updateImdbId");
        try {
            final List<Movie> movies = integrationRepository.listMovieWithoutTimdbId();
            for (final Movie movie : movies) {
                try {
                    imdbAPI.updateMovieInformation(movie, DateUtils.getCurrentYear());
                    integrationRepository.saveMovie(movie);
                } catch (CannotFindIMDIdException ex) {
                    ExceptionManager.log(ex, "Information from IMDB not found for the movie %s", movie.getTitle());
                } catch (Exception ex) {
                    ExceptionManager.log(ex, "Exception on getting Information from IMDB the movie %s", movie.getTitle());
                }
            }
        } finally {
            MetricManager.stopTimer(timer);
        }
    }

    /**
     * Update Rotten tomatoes Rating for the movies that don't have any
     */
    private void updateRottenTomatoesInformation() {
        final Optional<Timer.Context> timer = MetricManager.startTimer(className, "updateRottenTomatoesInformation");
        try {
            final List<Movie> movies = integrationRepository.listMovieWithoutRottenTomatoesRating();
            for (final Movie movie : movies) {
                try {
                    rottenTomatoesAPI.updateMovieInformation(movie);
                    integrationRepository.saveMovie(movie);
                } catch (CannotFindRottenTomatoesRatingException ex) {
                    ExceptionManager.log(ex, "Movie not found on RottenTomatoes API to get score updated, imdbID=%s title=%s",
                            movie.getImdbId(), movie.getTitle());
                } catch (Exception ex) {
                    ExceptionManager.log(ex, "Exception on getting RottenTomatoes Rating for movie with id=%s", movie.getId());
                }
            }
        } finally {
            MetricManager.stopTimer(timer);
        }
    }

    /**
     * Update movie rating and information from TrackTv API for the movie that don't have this information yet
     */
    private void updateTraktTvInformation() {
        final Optional<Timer.Context> timer = MetricManager.startTimer(className, "updateTraktTvInformation");
        try {
            final List<Movie> movies = integrationRepository.listMovieWithoutTrackTvInformation();
            for (final Movie movie : movies) {
                try {
                    traktTvAPI.updateMovieInformation(movie);
                    integrationRepository.saveMovie(movie);
                } catch (CannotFindTrackTvInformationException ex) {
                    ExceptionManager.log(ex, "Movie not found on TrackTv API using tImdbID=%s title=%s",
                            movie.getTimdbId(), movie.getTitle());
                } catch (Exception ex) {
                    ExceptionManager.log(ex, "Exception on getting TrackTv information for movie with id=%s", movie.getId());
                }
            }
        } finally {
            MetricManager.stopTimer(timer);
        }
    }

    public boolean updateComingSoonMovie() {
        List<Movie> movies = alloCineAPI.findComingSoon();
        for (Movie movie : movies) {
            integrationRepository.saveMovie(movie);
        }
        return true;
    }

    public List<Movie> listMovieWithoutImdb() {
        return integrationRepository.listMovieWithoutTimdbId();
    }

    public List<Movie> listMovieWithoutRottenRating() {
        return integrationRepository.listMovieWithoutRottenTomatoesRating();
    }

    public List<Movie> listMovieWithoutTrackTv() {
        return integrationRepository.listMovieWithoutTrackTvInformation();
    }
}
