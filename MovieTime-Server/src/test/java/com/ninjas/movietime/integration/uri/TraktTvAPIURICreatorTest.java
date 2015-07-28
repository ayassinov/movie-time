package com.ninjas.movietime.integration.uri;

import com.google.common.collect.ImmutableList;
import com.ninjas.movietime.integration.helpers.Parameter;
import org.hamcrest.Matchers;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.runners.MockitoJUnitRunner;

import java.net.MalformedURLException;
import java.net.URI;

@RunWith(MockitoJUnitRunner.class)
public class TraktTvAPIURICreatorTest {

    private URICreator uriCreator;

    @Before
    public void init() {
        uriCreator = new TraktTvAPIURICreator();
    }


    @Test(expected = IndexOutOfBoundsException.class)
    public void testNoParametersCreateUri() {
        uriCreator.create("movie/summary.json", ImmutableList.<Parameter>builder().build());
        Assert.fail();
    }


    @Test
    public void testCreateUri() throws MalformedURLException {
        final String expectedUrl = "http://api.trakt.tv/movie/summary.json/6d98ac69d9f35b06b303d570988c72ff/262391";
        final URI uri = uriCreator.create("movie/summary", ImmutableList.of(new Parameter("id", "262391")));
        Assert.assertThat(uri, Matchers.notNullValue());
        Assert.assertThat(uri.toURL().toString(), Matchers.equalTo(expectedUrl));
    }

}