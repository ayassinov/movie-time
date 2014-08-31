package com.ninjas.movietime.integration;

import com.ninjas.movietime.BaseTest;
import com.ninjas.movietime.core.domain.movie.Movie;
import org.hamcrest.Matchers;
import org.junit.Assert;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

public class ImdbAPITest extends BaseTest {

    @Autowired
    private ImdbAPI imdbAPI;

    @Test
    public void testFindImdbCode() {
        final Movie movie = new Movie();
        imdbAPI.updateMovieInformation(movie, "le role de ma vie", 2014);
        Assert.assertThat(movie.getImdbId(), Matchers.notNullValue());
    }
}