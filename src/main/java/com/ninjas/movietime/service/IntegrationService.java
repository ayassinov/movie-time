package com.ninjas.movietime.service;

import com.codahale.metrics.Timer;
import com.google.common.base.Optional;
import com.ninjas.movietime.core.domain.APICallLog;
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
import org.springframework.data.mongodb.core.MongoTemplate;
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
    private final MongoTemplate mongoTemplate;
    private final AlloCineAPI alloCineAPI;
    private final ImdbAPI imdbAPI;
    private final RottenTomatoesAPI rottenTomatoesAPI;
    private final TraktTvAPI traktTvAPI;

    @Autowired
    public IntegrationService(IntegrationRepository integrationRepository, MongoTemplate mongoTemplate,
                              AlloCineAPI alloCineAPI,
                              ImdbAPI imdbAPI,
                              RottenTomatoesAPI rottenTomatoesAPI,
                              TraktTvAPI traktTvAPI) {
        this.integrationRepository = integrationRepository;
        this.mongoTemplate = mongoTemplate;
        this.alloCineAPI = alloCineAPI;
        this.imdbAPI = imdbAPI;
        this.rottenTomatoesAPI = rottenTomatoesAPI;
        this.traktTvAPI = traktTvAPI;
    }

    /**
     * Add new/Update Theater and TheaterChain from AlloCineAPI
     */
    public void updateTheaters() {
        final Optional<Timer.Context> timer = MetricManager.startTimer(className, "updateTheaters");
        try {
            final List<Theater> allByRegion = alloCineAPI.findAllInParis();
            integrationRepository.saveTheater(allByRegion);
            integrationRepository.saveAPICallLog(APICallLog.success(APICallLog.OperationEnum.THEATER_UPDATE));
            log.debug("List of all theaters in paris {} updated", allByRegion.size());
        } catch (Exception ex) {
            ExceptionManager.log(ex, "Exception On Updating Movie Theaters");
            integrationRepository.saveAPICallLog(APICallLog.fail(APICallLog.OperationEnum.THEATER_UPDATE));
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
    public void updateMovieShowtime(boolean isTrackedOnly) {
        updateShowtime(isTrackedOnly);
        updateImdbId();
        updateTraktTvInformation();
        updateRottenTomatoesInformation();
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
                final List<Theater> theaters = integrationRepository.listOpenTheaterByTheaterChain(theaterChain, true);
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
                if (imdbAPI.updateMovieInformation(movie, DateUtils.getCurrentYear()))
                    mongoTemplate.save(movie);
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
                if (rottenTomatoesAPI.updateMovieInformation(movie)) {
                    mongoTemplate.save(movie);
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
                if (traktTvAPI.updateMovieInformation(movie)) {
                    integrationRepository.saveMovie(movie);
                }
            }
        } finally {
            MetricManager.stopTimer(timer);
        }
    }

}
