package com.ninjas.movietime.service;

import com.ninjas.movietime.BaseTest;
import org.hamcrest.Matchers;
import org.junit.Assert;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * @author ayassinov on 27/08/2014.
 */
public class IntegrationServiceTest extends BaseTest {

    @Autowired
    private IntegrationService integrationService;

    @Test
    //@Ignore
    public void testIntegrateTheaters() {
        integrationService.integrateTheaters();
        Assert.assertThat(true, Matchers.is(true));
    }

    @Test
    //@Ignore
    public void testUpdateShowtime() {
        integrationService.updateShowtime();
        Assert.assertThat(true, Matchers.is(true));
    }

    @Test
    //@Ignore
    public void testUpdateImdbMovieCode() {
        integrationService.updateImdbCode();
        Assert.assertThat(true, Matchers.is(true));

    }


    @Test
    //@Ignore
    public void testUpdateRottenTomatoesCode() {
        integrationService.updateRottenTomatoesCode();
        Assert.assertThat(true, Matchers.is(true));
    }

    @Test
    //@Ignore
    public void testUpdateTraktTvInformation() {
        integrationService.updateTraktTvInformation();
        Assert.assertThat(true, Matchers.is(true));
    }
}
