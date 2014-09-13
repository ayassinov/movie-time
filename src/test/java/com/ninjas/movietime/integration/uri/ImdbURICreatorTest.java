package com.ninjas.movietime.integration.uri;

import com.google.common.collect.ImmutableList;
import com.ninjas.movietime.integration.helpers.Parameter;
import org.hamcrest.Matchers;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.runners.MockitoJUnitRunner;

import java.net.URI;


@RunWith(MockitoJUnitRunner.class)
public class ImdbURICreatorTest {

    private URICreator uriCreator;

    @Before
    public void setUp() throws Exception {
        uriCreator = new ImdbURICreator();
    }

    @Test
    public void testCreate() throws Exception {
        final String exceptedUrl = "https://api.themoviedb.org/3/search/movie?api_key=e478c264afe3a1f9fb058c8059cedd78" +
                "&query=Plane2&year=2012";
        final URI uri = uriCreator.create("search/movie",
                ImmutableList.of(new Parameter("query", "Plane2"), new Parameter("year", "2012")));
        Assert.assertThat(uri, Matchers.notNullValue());
        Assert.assertThat(uri.toURL().toString(), Matchers.equalTo(exceptedUrl));
    }
}