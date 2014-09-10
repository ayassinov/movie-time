package com.ninjas.movietime.repository;

import com.google.common.base.Preconditions;
import com.ninjas.movietime.core.domain.movie.Movie;
import com.ninjas.movietime.core.util.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @author ayassinov on 29/08/2014.
 */
@Repository
public class MovieRepository {

    private final MongoTemplate mongoTemplate;

    @Autowired
    public MovieRepository(MongoTemplate mongoTemplate) {
        this.mongoTemplate = mongoTemplate;
    }

    public List<Movie> listComingSoon(int page, int countPerPage) {
        Preconditions.checkArgument(countPerPage > 0, "countPerPage cannot be negative or equal to Zero");
        final Query query = Query.query(Criteria.where("releaseDate").lt(DateUtils.nowParisDateTime())).skip(page * countPerPage - 1).limit(countPerPage);
        return mongoTemplate.find(query, Movie.class);
    }
}
