package com.ninjas.movietime.resource.manage;

import com.google.common.base.Strings;
import com.ninjas.movietime.core.domain.movie.Movie;
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
    public List<Movie> moviesWithProblem(@RequestParam(value = "name", required = false) String propertyName){
        if(Strings.isNullOrEmpty(propertyName))
            return statusService.listMovieNotFullyUpdated();
        return statusService.listMovieWithPropertyNotSet(propertyName);
    }
}
