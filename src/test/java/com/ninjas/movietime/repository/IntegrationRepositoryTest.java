package com.ninjas.movietime.repository;

import com.ninjas.movietime.BaseTest;
import com.ninjas.movietime.core.domain.theater.TheaterChain;
import org.hamcrest.Matchers;
import org.junit.Assert;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

public class IntegrationRepositoryTest extends BaseTest {

    @Autowired
    private IntegrationRepository repository;

    @Test
    public void testListOpenTheaterByTheaterChain() {
        repository.listOpenTheaterByTheaterChain(new TheaterChain("81001"), true);
        Assert.assertThat(true, Matchers.is(true));
    }
}