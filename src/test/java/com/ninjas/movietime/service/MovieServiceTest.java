package com.ninjas.movietime.service;

import com.ninjas.movietime.BaseTest;
import com.ninjas.movietime.core.domain.showtime.Showtime;
import com.ninjas.movietime.core.domain.theater.Theater;
import org.hamcrest.Matchers;
import org.junit.Assert;
import org.junit.Ignore;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;

import java.util.List;

public class MovieServiceTest extends BaseTest {

    @Autowired
    private MongoTemplate mongoTemplate;

    @Test
    @Ignore
    public void testFindDistinctGenre() {
        //B2619

        //final List<Showtime> showtimes = mongoTemplate.find(Query.query(Criteria.where("movie").is(new Movie("205445"))), Showtime.class);
        final List<Showtime> theater = mongoTemplate.find(
                Query.query(Criteria.where("theater").is(new Theater("B2619")).and("schedules.language.isVO").is(true)
                ), Showtime.class);


        //final List<Movie> all = mongoTemplate.findAll(Movie.class);
        //noinspection unchecked
        //final List<String> movies = mongoTemplate.getCollection("movies").distinct("genres.name");
        //select all movies with showtimes and theater


        //


        Assert.assertThat(true, Matchers.is(true));
    }
}