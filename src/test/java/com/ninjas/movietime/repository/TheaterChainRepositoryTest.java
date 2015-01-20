package com.ninjas.movietime.repository;

import com.google.common.base.Optional;
import com.ninjas.movietime.BaseTest;
import com.ninjas.movietime.core.domain.theater.TheaterChain;
import org.hamcrest.Matchers;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;

import static org.hamcrest.Matchers.*;
import static org.junit.Assert.assertThat;

/**
 * @author ayassinov on 19/01/2015.
 */
public class TheaterChainRepositoryTest extends BaseTest {

    @Autowired
    private TheaterChainRepository theaterChainRepository;

    private final TheaterChain savedTheaterChain = new TheaterChain("111", "UGC");

    @Before
    public void init() {
        //add at least one Theater Chain
        theaterChainRepository.getMongoTemplate().save(savedTheaterChain);
    }

    @Test
    public void listAllTest() {
        final Page<TheaterChain> theaterChains = theaterChainRepository.listAll(new PageRequest(0, 10));
        assertThat(theaterChains.getContent(), not(empty()));
        assertThat(theaterChains.getContent(), hasItem(savedTheaterChain));
    }

    @Test(expected = NullPointerException.class)
    public void testGetByIdParameterIsNull() {
        theaterChainRepository.getById(null);
    }

    @Test()
    public void testGetByIdNotFound() {
        final Optional<TheaterChain> chainOptional = theaterChainRepository.getById("0000");
        assertThat(chainOptional.isPresent(), Matchers.is(false));
        assertThat(chainOptional.orNull(), is(nullValue()));
    }

    @Test()
    public void testGetByIdFound() {
        final Optional<TheaterChain> chainOptional = theaterChainRepository.getById("111");
        assertThat(chainOptional.isPresent(), Matchers.is(true));
        assertThat(chainOptional.get(), not(is(nullValue())));
        assertThat(chainOptional.get().getId(), is("111"));
        assertThat(chainOptional.get().getName(), is("UGC"));
    }
}
