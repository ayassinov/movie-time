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
public class RottenTomatoesURICreatorTest {

    private URICreator uriCreator;

    @Before
    public void setUp() throws Exception {
        uriCreator = new RottenTomatoesURICreator();
    }

    @Test
    public void testCreate() throws Exception {
        final String expectedUrl = "http://api.rottentomatoes.com/api/public/v1.0/" +
                "movie_alias.json?type=imdb&id=2870708&apikey=n6g8dqcjqb3bu5ze9xavztsx";
        final URI uri = uriCreator.create("movie_alias", ImmutableList.of(new Parameter("type", "imdb"),
                new Parameter("id", "2870708")));

        Assert.assertThat(uri, Matchers.notNullValue());
        Assert.assertThat(uri.toURL().toString(), Matchers.equalTo(expectedUrl));
    }
}