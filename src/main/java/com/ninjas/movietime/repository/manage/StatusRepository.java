package com.ninjas.movietime.repository.manage;

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
        final Query query = Query.query(Criteria.where(property).is(true));
        final List<Movie> movies = mongoTemplate.find(query, Movie.class);
        log.debug("Found {} Movies without critical information", movies.size());
        return movies;
    }


    public List<Movie> moviesWithoutCompleteInformation() {
        final Query query = Query.query(Criteria.where("movieUpdateStatus.isAlloCineFullUpdated").is(false));
        final List<Movie> movies = mongoTemplate.find(query, Movie.class);
        log.debug("Found {} Movies not fully updated from external APIs", movies.size());
        return movies;
    }
}
