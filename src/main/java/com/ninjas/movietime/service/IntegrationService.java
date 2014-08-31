package com.ninjas.movietime.service;

import com.ninjas.movietime.core.domain.People;
import com.ninjas.movietime.core.domain.UpdateTracking;
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
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author ayassinov on 27/08/2014.
 */
@Service
public class IntegrationService {

    private static final Logger LOG = LoggerFactory.getLogger(IntegrationService.class);

    private final MongoTemplate mongoTemplate;
    private final AlloCineAPI alloCineAPI;
    private final ImdbAPI imdbAPI;
    private final RottenTomatoesAPI rottenTomatoesAPI;
    private final TraktTvAPI traktTvAPI;

    @Autowired
    public IntegrationService(MongoTemplate mongoTemplate, AlloCineAPI alloCineAPI, ImdbAPI imdbAPI, RottenTomatoesAPI rottenTomatoesAPI, TraktTvAPI traktTvAPI) {
        this.mongoTemplate = mongoTemplate;
        this.alloCineAPI = alloCineAPI;
        this.imdbAPI = imdbAPI;
        this.rottenTomatoesAPI = rottenTomatoesAPI;
        this.traktTvAPI = traktTvAPI;
    }

    public void integrateTheaters() {
        final List<Theater> allByRegion = alloCineAPI.findAllInParis();
        for (final Theater theater : allByRegion) {
            mongoTemplate.save(theater.getTheaterChain());
            mongoTemplate.save(theater);
        }
        //track update
        mongoTemplate.save(new UpdateTracking(UpdateTracking.OperationEnum.THEATER_UPDATE, true,
                "DONE WITH SUCCESS"));
    }

    public void updateShowtime() {
        //find official theaterChain
        final List<TheaterChain> theaterChains = listAllTheaterChain(true);
        //iterate over every chain to get the theaters list
        for (final TheaterChain theaterChain : theaterChains) {
            if (theaterChain.getId().equalsIgnoreCase("81001")) {
                final List<Theater> theaters = listOpenTheaterByTheaterChain(theaterChain);
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

    private List<TheaterChain> listAllTheaterChain(boolean isOnlyTracked) {
        final List<TheaterChain> theaterChains;
        if (isOnlyTracked) {
            final Query theaterChainQuery = Query.query(Criteria.where("isTracked").is(true));
            theaterChains = mongoTemplate.find(theaterChainQuery, TheaterChain.class);
        } else {
            theaterChains = mongoTemplate.findAll(TheaterChain.class);
        }
        return theaterChains;
    }

    private List<Theater> listOpenTheaterByTheaterChain(TheaterChain theaterChain) {
        final Query theaterQuery = Query.query(Criteria.where("theaterChain").is(theaterChain).and("isOpen").is(true));
        return mongoTemplate.find(theaterQuery, Theater.class);
    }

   /* public void updateMovies() {

    }

    public void integrateUpComingMovies() {

    }*/

}
