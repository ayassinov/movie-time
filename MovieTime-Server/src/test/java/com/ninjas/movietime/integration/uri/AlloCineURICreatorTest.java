package com.ninjas.movietime.integration.uri;

import com.google.common.collect.ImmutableList;
import com.ninjas.movietime.integration.helpers.Parameter;
import com.ninjas.movietime.integration.helpers.UrlSigner;
import org.hamcrest.Matchers;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.net.URI;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class AlloCineURICreatorTest {


    private AlloCineURICreator uriCreator;

    @Before
    public void setUp() throws Exception {
        final UrlSigner mockedUrlSigner = mock(UrlSigner.class);
        when(mockedUrlSigner.getSed()).thenReturn("&sed=20140913");
        when(mockedUrlSigner.getSig()).thenReturn("&sig=LsNRS6jCzLbvnCnMDCrM1lzUAPc%3D");
        uriCreator = new AlloCineURICreator();
        uriCreator.setUrlSigner(mockedUrlSigner);
    }

    @Test
    public void testCreate() throws Exception {
        final String expectedUrl = "http://api.allocine.fr/rest/v3/movie?code=2323&format=json&partner=100043982026" +
                "&profile=medium&sed=20140913&sig=LsNRS6jCzLbvnCnMDCrM1lzUAPc%3D";
        final URI uri = uriCreator.create("movie", ImmutableList.of(new Parameter("code", "2323")));
        Assert.assertThat(uri, Matchers.notNullValue());
        Assert.assertThat(uri.toURL().toString(), Matchers.equalTo(expectedUrl));

    }
}