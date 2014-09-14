package com.ninjas.movietime.service;

import com.codahale.metrics.Timer;
import com.google.common.base.Optional;
import com.ninjas.movietime.core.domain.movie.Movie;
import com.ninjas.movietime.core.util.MetricManager;
import com.ninjas.movietime.repository.MovieRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author ayassinov on 29/08/2014.
 */
@Service
public class MovieService {

    private final String className = this.getClass().getCanonicalName();

    private final MovieRepository movieRepository;

    @Autowired
    public MovieService(MovieRepository movieRepository) {
        this.movieRepository = movieRepository;
    }

    public List<Movie> listComingSoon(int page, int countPerPage) {
        final Optional<Timer.Context> timer = MetricManager.startTimer(className, "getAppInformation");
        try {
            return movieRepository.listComingSoon(page, countPerPage);
        } finally {
            MetricManager.stopTimer(timer);
        }
    }

}
