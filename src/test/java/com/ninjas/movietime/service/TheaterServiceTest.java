package com.ninjas.movietime.service;

import com.google.common.base.Optional;
import com.google.common.collect.ImmutableList;
import com.ninjas.movietime.core.domain.theater.TheaterChain;
import com.ninjas.movietime.repository.TheaterChainRepository;
import com.ninjas.movietime.repository.TheaterRepository;
import org.hamcrest.Matchers;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;

import static org.hamcrest.Matchers.*;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class TheaterServiceTest {

    private final TheaterChainRepository theaterChainRepository = mock(TheaterChainRepository.class);
    private final TheaterRepository theaterRepository = mock(TheaterRepository.class);
    private TheaterService theaterService = null;

    private TheaterChain savedTheaterChain = new TheaterChain("1111", "UGC");

    @Before
    public void init() {
        if (theaterService != null)
            return;
        theaterService = new TheaterService(theaterRepository, theaterChainRepository);
    }


    @Test(expected = IllegalArgumentException.class)
    public void testListAllFailPageParam() {
        theaterService.listAllTheaterChain(-1, 10);
    }


    @Test(expected = IllegalArgumentException.class)
    public void testListAllFailSizeParam() {
        theaterService.listAllTheaterChain(0, 0);
    }

    @Test
    public void testListAll() {
        when(theaterChainRepository.listAll(any(PageRequest.class)))
                .thenReturn(new PageImpl<>(ImmutableList.of(savedTheaterChain)));

        final Page<TheaterChain> theaterChains = theaterService.listAllTheaterChain(0, 1);
        Assert.assertThat(theaterChains.getContent(), not(empty()));
        Assert.assertThat(theaterChains.getContent(), Matchers.hasItem(savedTheaterChain));

    }

    @Test
    public void testGetTheaterChain() {

        when(theaterChainRepository.getById(anyString()))
                .thenReturn(Optional.of(savedTheaterChain))
                .thenReturn(Optional.<TheaterChain>absent());

        final Optional<TheaterChain> theaterChain = theaterService.getTheaterChain("1111");
        Assert.assertThat(theaterChain.isPresent(), is(true));
        Assert.assertThat(theaterChain.get().getId(), is("1111"));

        final Optional<TheaterChain> theaterChainNotExists = theaterService.getTheaterChain("0000");
        Assert.assertThat(theaterChainNotExists.isPresent(), is(false));
        Assert.assertThat(theaterChainNotExists.orNull(), is(nullValue()));
    }


}