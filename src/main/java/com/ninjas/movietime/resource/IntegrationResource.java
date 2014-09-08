package com.ninjas.movietime.resource;

import com.ninjas.movietime.core.domain.movie.Movie;
import com.ninjas.movietime.service.IntegrationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * @author ayassinov on 08/09/14.
 */
@RestController
@RequestMapping(value = "/manage/integration", produces = "application/json;utf-8")
public class IntegrationResource {

    private final IntegrationService integrationService;

    @Autowired
    public IntegrationResource(IntegrationService integrationService) {
        this.integrationService = integrationService;
    }

    @RequestMapping(value = "/movie/noImdb")
    public List<Movie> movieWithoutImdb(){
       return integrationService.listMovieWithoutImdb();
    }

    @RequestMapping(value = "/movie/noRotten")
    public List<Movie> movieWithoutRottenRating(){
        return integrationService.listMovieWithoutRottenRating();
    }

    @RequestMapping(value = "/movie/noTrackTv")
    public List<Movie> movieWithoutTrackTv(){
        return integrationService.listMovieWithoutTrackTv();
    }
}
