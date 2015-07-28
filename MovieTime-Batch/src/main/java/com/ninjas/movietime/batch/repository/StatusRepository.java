package com.ninjas.movietime.batch.repository;

import com.ninjas.movietime.core.domain.movie.Movie;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @author ayassinov on 13/09/14.
 */
@Slf4j
@Repository
public class StatusRepository {

    private final MongoTemplate mongoTemplate;

    @Autowired
    public StatusRepository(MongoTemplate mongoTemplate) {
        this.mongoTemplate = mongoTemplate;
    }


    public List<Movie> moviesWithPropertyNull(String property) {
        final Query query = Query.query(Criteria.where(property).is(null));
        final List<Movie> movies = mongoTemplate.find(query, Movie.class);
        log.debug("Found {} Movies without {}", movies.size(), property);
        return movies;
    }

    public List<Movie> listMovieWithoutTimdbId() {
        final Query query = Query.query(Criteria.where("timdbId").is(null));
        final List<Movie> movies = mongoTemplate.find(query, Movie.class);
        log.debug("Found {} Movies without timdb Id", movies.size());
        return movies;
    }

    public List<Movie> listMovieWithoutRottenTomatoesRating() {
        final Query query = Query.query(Criteria.where("rottenTomatoesId").is(null).and("imdbId").ne(null));

        final List<Movie> movies = mongoTemplate.find(query, Movie.class);
        log.debug("Found {} Movies without Rotten tomatoes rating", movies.size());
        return movies;
    }

    public List<Movie> listMovieWithoutTrackTvInformation() {
        final Query query = Query.query(Criteria.where("trackTvId").is(null).and("timdbId").ne(null));
        final List<Movie> movies = mongoTemplate.find(query, Movie.class);
        log.debug("Found {} Movies without trackTV information and rating", movies.size());
        return movies;
    }

    public List<Movie> listMovieNotFullyUpdated() {
        final Query query = Query.query(Criteria.where("movieUpdateStatus.isAlloCineFullUpdated").is(false));
        final List<Movie> movies = mongoTemplate.find(query, Movie.class);
        log.debug("Found {} Movies not fully updated from alloCine API", movies.size());
        return movies;
    }
}
