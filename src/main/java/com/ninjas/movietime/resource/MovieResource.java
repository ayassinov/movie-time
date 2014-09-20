package com.ninjas.movietime.resource;

import com.ninjas.movietime.core.domain.movie.Movie;
import com.ninjas.movietime.core.util.MetricManager;
import com.ninjas.movietime.service.MovieService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author ayassinov on 10/09/14.
 */
@RestController
@RequestMapping("api/v1/movie")
public class MovieResource {

    private final MovieService movieService;

    @Autowired
    public MovieResource(MovieService movieService) {
        this.movieService = movieService;
    }


    @RequestMapping("/comingsoon")
    public Page<Movie> listComingSoon(@RequestParam(value = "page", required = true, defaultValue = "0") int page,
                                                                      @RequestParam(value = "count", required = true, defaultValue = "10") int count) {
        MetricManager.markResourceMeter("movie", "comingsoon");
        return movieService.listComingSoon(page, count);
    }

    @RequestMapping("/thisweek")
    public Page<Movie> listThisWeek(@RequestParam(value = "page", required = true, defaultValue = "0") int page,
                                      @RequestParam(value = "count", required = true, defaultValue = "10") int count) {
        MetricManager.markResourceMeter("movie", "thisweek");
        return movieService.listOpeningThisWeek(page, count);
    }
}
