package com.ninjas.movietime.service;

import com.codahale.metrics.Timer;
import com.google.common.base.Optional;
import com.ninjas.movietime.core.domain.movie.Movie;
import com.ninjas.movietime.core.util.MetricManager;
import com.ninjas.movietime.repository.MovieRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

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

    public Page<Movie> listComingSoon(int page, int countPerPage) {
        final Optional<Timer.Context> timer = MetricManager.startTimer(className, "listComingSoon");
        try {
            return movieRepository.listComingSoon(page, countPerPage);
        } finally {
            MetricManager.stopTimer(timer);
        }
    }

    public Page<Movie> listOpeningThisWeek(int page, int countPerPage) {
        final Optional<Timer.Context> timer = MetricManager.startTimer(className, "listOpeningThisWeek");
        try {
            return movieRepository.listOpeningThisWeek(page, countPerPage);
        } finally {
            MetricManager.stopTimer(timer);
        }
    }
}
