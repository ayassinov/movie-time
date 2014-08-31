package com.ninjas.movietime.integration;

import com.google.common.collect.ImmutableList;
import com.ninjas.movietime.BaseTest;
import org.hamcrest.Matchers;
import org.junit.Assert;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

public class TraktTvAPITest extends BaseTest {


    @Autowired
    private TraktTvAPI traktTvAPI;

    @Test
    public void testGetMovieDetail() throws Exception {
        traktTvAPI.getMovieDetail("tt2800240");
        Assert.assertThat(true, Matchers.is(true));

    }

    @Test
    public void testGetMoviesDetail() throws Exception {
        traktTvAPI.getMoviesDetail(ImmutableList.of("tt2800240", "tt1821658"));
        Assert.assertThat(true, Matchers.is(true));

    }
}