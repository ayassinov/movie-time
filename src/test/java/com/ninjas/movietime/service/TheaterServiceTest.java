package com.ninjas.movietime.service;

import com.ninjas.movietime.BaseTest;
import com.ninjas.movietime.core.domain.theater.Theater;
import com.ninjas.movietime.repository.TheaterRepository;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.Matchers.is;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class TheaterServiceTest extends BaseTest {

    private TheaterService theaterService;

    @Before
    private void init() {
        final TheaterRepository theaterRepository = mock(TheaterRepository.class);
        when(theaterRepository.findAll()).then((org.mockito.stubbing.Answer<?>) new ArrayList<Theater>());
        theaterService = new TheaterService(theaterRepository);
    }

    @Test()
    public void testListAll() {
        final List<Theater> theaters = theaterService.listAll();
        Assert.assertThat(theaters.size(), is(0));
    }
}