package com.ninjas.movietime.resource.manage;

import com.google.common.base.Strings;
import com.ninjas.movietime.core.domain.movie.Movie;
import com.ninjas.movietime.core.util.MetricManager;
import com.ninjas.movietime.service.manage.StatusService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * @author ayassinov on 13/09/14.
 */
@RestController
@RequestMapping("/manage/status")
public class StatusResource {

    private final StatusService statusService;

    @Autowired
    public StatusResource(StatusService statusService) {
        this.statusService = statusService;
    }

    @RequestMapping(value = "/movie", method = RequestMethod.GET)
    public List<Movie> moviesWithProblem(@RequestParam(value = "name", required = false) String propertyName) {
        MetricManager.markResourceMeter("manage", "status", "movie");
        if (Strings.isNullOrEmpty(propertyName))
            return statusService.listMovieNotFullyUpdated();
        return statusService.listMovieWithPropertyNotSet(propertyName);
    }

    @RequestMapping(value = "/movie/noImdb")
    public List<Movie> movieWithoutImdb() {
        MetricManager.markResourceMeter("manage", "status", "movie", "noImdb");
        return statusService.listMovieWithoutImdb();
    }

    @RequestMapping(value = "/movie/noRotten")
    public List<Movie> movieWithoutRottenRating() {
        MetricManager.markResourceMeter("manage", "status", "movie", "noRotten");
        return statusService.listMovieWithoutRottenRating();
    }

    @RequestMapping(value = "/movie/noTrackTv")
    public List<Movie> movieWithoutTrackTv() {
        MetricManager.markResourceMeter("manage", "status", "movie", "noTrackTv");
        return statusService.listMovieWithoutTrackTv();
    }
}
