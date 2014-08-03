package com.ninjas.movietime.service;

import com.ninjas.movietime.BaseTest;
import com.ninjas.movietime.core.domain.Theater;
import org.junit.Assert;
import org.junit.Ignore;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

import static org.hamcrest.core.Is.is;

public class TheaterServiceTest extends BaseTest {

    @Autowired
    private TheaterService theaterService;

    @Test()
    public void testUpdate()  {
        List<Theater> theaters = this.theaterService.update();
        Assert.assertThat(theaters.size(), is(314));
    }
}