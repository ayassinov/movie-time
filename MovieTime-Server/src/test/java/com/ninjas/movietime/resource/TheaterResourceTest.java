package com.ninjas.movietime.resource;

import com.google.common.base.Optional;
import com.ninjas.movietime.BaseTest;
import com.ninjas.movietime.core.domain.theater.TheaterChain;
import org.junit.Ignore;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.TestRestTemplate;
import org.springframework.web.client.RestTemplate;

import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.nullValue;
import static org.junit.Assert.assertThat;

/**
 * @author ayassinov on 27/08/2014.
 */
@Ignore
public class TheaterResourceTest extends BaseTest {

    @Autowired
    TheaterResource theaterResource;

    RestTemplate template = new TestRestTemplate();

    @Test(expected = NullPointerException.class)
    public void testGetTheaterChainParameterNull() {
        final Optional<TheaterChain> theaterChain = theaterResource.getTheaterChain(null);
        assertThat(theaterChain, is(nullValue()));
    }

    @Test
    public void testGetTheaterChain() {
        final Optional<TheaterChain> theaterChain = theaterResource.getTheaterChain("1111");
        assertThat(theaterChain.isPresent(), is(false));

        final TheaterChain forObject = template.getForObject("http://127.0.0.1:8080/api/v1/theater/chain/1111", TheaterChain.class);
    }


}
