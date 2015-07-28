package com.ninjas.movietime.batch.service;

import com.ninjas.movietime.batch.integration.AlloCineAPI;
import com.ninjas.movietime.batch.integration.ImdbAPI;
import com.ninjas.movietime.batch.integration.RottenTomatoesAPI;
import com.ninjas.movietime.batch.integration.TraktTvAPI;
import com.ninjas.movietime.batch.integration.exception.CannotFindIMDIdException;
import com.ninjas.movietime.batch.integration.exception.CannotFindRottenTomatoesRatingException;
import com.ninjas.movietime.batch.integration.exception.CannotFindTrackTvInformationException;
import com.ninjas.movietime.batch.repository.IntegrationRepository;
import com.ninjas.movietime.batch.repository.StatusRepository;
import com.ninjas.movietime.core.domain.movie.Movie;
import com.ninjas.movietime.core.domain.showtime.Showtime;
import com.ninjas.movietime.core.domain.theater.Theater;
import com.ninjas.movietime.core.domain.theater.TheaterChain;
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
    private final StatusRepository statusRepository;
    private final AlloCineAPI alloCineAPI;
    private final ImdbAPI imdbAPI;
    private final RottenTomatoesAPI rottenTomatoesAPI;
    private final TraktTvAPI traktTvAPI;

    @Autowired
    public IntegrationService(IntegrationRepository integrationRepository, StatusRepository statusRepository,
                              AlloCineAPI alloCineAPI, ImdbAPI imdbAPI, RottenTomatoesAPI rottenTomatoesAPI, TraktTvAPI traktTvAPI) {
        this.integrationRepository = integrationRepository;
        this.statusRepository = statusRepository;
        this.alloCineAPI = alloCineAPI;
        this.imdbAPI = imdbAPI;
        this.rottenTomatoesAPI = rottenTomatoesAPI;
        this.traktTvAPI = traktTvAPI;
    }

    /**
     * Add new/Update Theater and TheaterChain from AlloCineAPI
     */
    public boolean updateTheaters() {

        // final Optional<Timer.Context> timer = MetricManager.startTimer(className, "updateTheaters");
        try {
            final List<Theater> allByRegion = alloCineAPI.findAllInParis();
            integrationRepository.saveTheater(allByRegion);
            log.debug("List of all theaters in paris {} updated", allByRegion.size());
            return true;
        } catch (Exception ex) {
            //   ExceptionManager.log(ex, "Exception On Updating Movie Theaters");
            return false;
        } finally {
            //  MetricManager.stopTimer(timer);
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
        updateMovieFullDetail();
        updateImdbId();
        updateTraktTvInformation();
        updateRottenTomatoesInformation();
        return true;
    }

    /**
     * Create or Update coming soon movies
     *
     * @return true if the operation ended successfully
     */
    public boolean updateComingSoonMovie() {
        List<Movie> movies = alloCineAPI.findComingSoon();
        for (Movie movie : movies) {
            integrationRepository.saveMovie(movie);
        }

        updateMovieFullDetail();
        updateImdbId();
        updateTraktTvInformation();
        return true;
    }

    /**
     * update the movie with full details from allo cine API
     */
    private void updateMovieFullDetail() {
        //select movies without full information
        // final Optional<Timer.Context> timer = MetricManager.startTimer(className, "updateMovieFullDetail");
        try {
            final List<Movie> movies = statusRepository.listMovieNotFullyUpdated();
            for (Movie movie : movies) {
                try {
                    alloCineAPI.updateFullMovieInformation(movie);
                    integrationRepository.saveMovie(movie);
                    Thread.sleep(5000); //Sorry for this !, wait 5 sec between calls, so AlloCine will not block us
                } catch (Exception ex) {
                    //         ExceptionManager.log(ex, "Full information from AlloCine not found for the movie id=%s title=%s",
                    //                 movie.getId(), movie.getTitle());
                }
            }
        } finally {
            //   MetricManager.stopTimer(timer);
        }
    }

    /**
     * Update showtime and movie information from alloCine API
     *
     * @param isTrackedOnly if true restrict the showtime only to the theater that we are tracking.
     */
    private void updateShowtime(boolean isTrackedOnly) {
        //final Optional<Timer.Context> timer = MetricManager.startTimer(className, "updateShowtime");
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
            //  MetricManager.stopTimer(timer);
        }
    }

    /**
     * update imdb code and rating for newly added Movie
     */
    private void updateImdbId() {
        // final Optional<Timer.Context> timer = MetricManager.startTimer(className, "updateImdbId");
        try {
            final List<Movie> movies = statusRepository.listMovieWithoutTimdbId();
            for (final Movie movie : movies) {
                try {
                    imdbAPI.updateMovieInformation(movie, movie.getReleaseDate().getYear());
                    integrationRepository.saveMovie(movie);
                } catch (CannotFindIMDIdException ex) {
                    //    ExceptionManager.log(ex, "Information from IMDB not found for the movie id=%s title=%s", movie.getId(), movie.getTitle());
                } catch (Exception ex) {
                    //   ExceptionManager.log(ex, "Exception on getting Information from IMDB the movie id=%s title=%s", movie.getId(), movie.getTitle());
                }
            }
        } finally {
            //  MetricManager.stopTimer(timer);
        }
    }

    /**
     * Update Rotten tomatoes Rating for the movies that don't have any
     */
    private void updateRottenTomatoesInformation() {
        //final Optional<Timer.Context> timer = MetricManager.startTimer(className, "updateRottenTomatoesInformation");
        try {
            final List<Movie> movies = statusRepository.listMovieWithoutRottenTomatoesRating();
            for (final Movie movie : movies) {
                try {
                    rottenTomatoesAPI.updateMovieInformation(movie);
                    integrationRepository.saveMovie(movie);
                } catch (CannotFindRottenTomatoesRatingException ex) {
                    //   ExceptionManager.log(ex, "Movie not found on RottenTomatoes API to get score updated, imdbID=%s title=%s",
                    //           movie.getImdbId(), movie.getTitle());
                } catch (Exception ex) {
                    //   ExceptionManager.log(ex, "Exception on getting RottenTomatoes Rating for movie with id=%s imdbID=%s title=%s", movie.getId()
                    //           , movie.getImdbId(), movie.getTitle());
                }
            }
        } finally {
            // MetricManager.stopTimer(timer);
        }
    }

    /**
     * Update movie rating and information from TrackTv API for the movie that don't have this information yet
     */
    private void updateTraktTvInformation() {
        //final Optional<Timer.Context> timer = MetricManager.startTimer(className, "updateTraktTvInformation");
        try {
            final List<Movie> movies = statusRepository.listMovieWithoutTrackTvInformation();
            for (final Movie movie : movies) {
                try {
                    traktTvAPI.updateMovieInformation(movie);
                    integrationRepository.saveMovie(movie);
                } catch (CannotFindTrackTvInformationException ex) {
                    //  ExceptionManager.log(ex, "Movie not found on TrackTv API using id=%s tImdbID=%s title=%s",
                    //          movie.getId(), movie.getTimdbId(), movie.getTitle());
                } catch (Exception ex) {
                    //   ExceptionManager.log(ex, "Exception on getting TrackTv information for movie with id=%s title=%s", movie.getId(), movie.getTitle());
                }
            }
        } finally {
            // MetricManager.stopTimer(timer);
        }
    }
}
