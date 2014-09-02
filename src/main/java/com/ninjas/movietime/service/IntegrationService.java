package com.ninjas.movietime.service;

import com.ninjas.movietime.core.domain.APICallLog;
import com.ninjas.movietime.core.domain.People;
import com.ninjas.movietime.core.domain.movie.Genre;
import com.ninjas.movietime.core.domain.movie.Movie;
import com.ninjas.movietime.core.domain.showtime.Showtime;
import com.ninjas.movietime.core.domain.theater.Theater;
import com.ninjas.movietime.core.domain.theater.TheaterChain;
import com.ninjas.movietime.core.util.DateUtils;
import com.ninjas.movietime.integration.AlloCineAPI;
import com.ninjas.movietime.integration.ImdbAPI;
import com.ninjas.movietime.integration.RottenTomatoesAPI;
import com.ninjas.movietime.integration.TraktTvAPI;
import com.ninjas.movietime.repository.IntegrationRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author ayassinov on 27/08/2014.
 */
@Slf4j
@Service
public class IntegrationService {

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
        try {
            final List<Theater> allByRegion = alloCineAPI.findAllInParis();
            integrationRepository.saveTheater(allByRegion);
            integrationRepository.saveAPICallLog(APICallLog.success(APICallLog.OperationEnum.THEATER_UPDATE));
        } catch (Exception ex) {
            log.error("Exception On Updating Movie Theaters", ex);
            integrationRepository.saveAPICallLog(APICallLog.fail(APICallLog.OperationEnum.THEATER_UPDATE));
        }
    }

    public void updateShowtime(boolean isTrackedOnly) {
        //find theater Chain
        final List<TheaterChain> theaterChains = integrationRepository.listAllTheaterChain(isTrackedOnly);
        //iterate over every chain to get the theaters list
        for (final TheaterChain theaterChain : theaterChains) {
            //list all theaters
            final List<Theater> theaters = integrationRepository.listOpenTheaterByTheaterChain(theaterChain, true);

            final List<Showtime> showtimes = alloCineAPI.findShowtime(theaters);

            //todo bulk save
            for (Showtime showtime : showtimes) {
                for (People actor : showtime.getMovie().getActors())
                    mongoTemplate.save(actor);

                for (People director : showtime.getMovie().getDirectors())
                    mongoTemplate.save(director);

                for (Genre genre : showtime.getMovie().getGenres()) {
                    mongoTemplate.save(genre);
                }

                mongoTemplate.save(showtime.getMovie());
                mongoTemplate.save(showtime);
            }
        }
    }

    public void updateImdbCode() {
        final List<Movie> movies = mongoTemplate.find(Query.query(Criteria.where("timdbLastUpdate").is(null)), Movie.class);
        for (final Movie movie : movies) {
            imdbAPI.updateMovieInformation(movie, movie.getTitle(), DateUtils.getCurrentYear());
            if (movie.getTimdbLastUpdate() != null) {
                mongoTemplate.save(movie);
                break;
            }
        }
    }

    public void updateRottenTomatoesCode() {
        final List<Movie> movies = mongoTemplate.find(
                Query.query(Criteria.where("rottenTomatoesLastUpdate").is(null).and("imdbId").ne(null)),
                Movie.class
        );
        for (final Movie movie : movies) {
            rottenTomatoesAPI.updateMovieInformation(movie, movie.getImdbId());
            if (movie.getRottenTomatoesLastUpdate() != null) {
                mongoTemplate.save(movie);
                break;
            }
        }
    }

    public void updateTraktTvInformation() {
        final List<Movie> movies = mongoTemplate.find(
                Query.query(Criteria.where("traktLastUpdate").is(null).and("timdbId").ne(null)),
                Movie.class
        );
        for (final Movie movie : movies) {
            traktTvAPI.updateMovieInformation(movie, movie.getTimdbId());
            if (movie.getTimdbLastUpdate() != null) {
                for (People actor : movie.getActors())
                    mongoTemplate.save(actor);

                for (People director : movie.getDirectors())
                    mongoTemplate.save(director);

                for (People writer : movie.getWriters())
                    mongoTemplate.save(writer);

                for (People producer : movie.getProducers())
                    mongoTemplate.save(producer);

                mongoTemplate.save(movie);
                break;
            }
        }
    }


    public void updateMovies() {

    }

    public void integrateUpComingMovies() {

    }

}
