package com.ninjas.movietime.resource;

import com.ninjas.movietime.core.domain.movie.Movie;
import com.ninjas.movietime.service.MovieService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * @author ayassinov on 10/09/14.
 */
@RestController
@RequestMapping("api/${movietime.app.apiVersion}/movie")
public class MovieResource {

    private final MovieService movieService;

    @Autowired
    public MovieResource(MovieService movieService) {
        this.movieService = movieService;
    }


    @RequestMapping("/coming")
    public List<Movie> listComingSoon(@RequestParam(value = "page", required = true, defaultValue = "1") int page,
                                      @RequestParam(value = "count", required = true, defaultValue = "10") int count) {
        return movieService.listComingSoon(page, count);
    }
}
