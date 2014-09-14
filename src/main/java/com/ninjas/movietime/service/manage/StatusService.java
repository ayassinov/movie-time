package com.ninjas.movietime.service.manage;

import com.codahale.metrics.Timer;
import com.google.common.base.Optional;
import com.google.common.base.Preconditions;
import com.ninjas.movietime.core.domain.movie.Movie;
import com.ninjas.movietime.core.util.MetricManager;
import com.ninjas.movietime.repository.manage.StatusRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author ayassinov on 13/09/14.
 */
@Service
public class StatusService {

    private final String className = this.getClass().getCanonicalName();

    private final StatusRepository statusRepository;

    @Autowired
    public StatusService(StatusRepository statusRepository) {
        this.statusRepository = statusRepository;
    }


    public List<Movie> listMovieWithPropertyNotSet(String property) {
        Preconditions.checkNotNull(property, "property name cannot be null");
        final Optional<Timer.Context> timer = MetricManager.startTimer(className, "listMovieWithPropertyNotSet");
        try {
            return statusRepository.moviesWithPropertyNull(property);
        } finally {
            MetricManager.stopTimer(timer);
        }
    }

    public List<Movie> listMovieNotFullyUpdated() {
        final Optional<Timer.Context> timer = MetricManager.startTimer(className, "listMovieNotFullyUpdated");
        try {
            return this.statusRepository.listMovieNotFullyUpdated();
        } finally {
            MetricManager.stopTimer(timer);

        }
    }

    public List<Movie> listMovieWithoutImdb() {
        final Optional<Timer.Context> timer = MetricManager.startTimer(className, "listMovieWithoutImdb");
        try {
            return statusRepository.listMovieWithoutTimdbId();
        } finally {
            MetricManager.stopTimer(timer);

        }
    }

    public List<Movie> listMovieWithoutRottenRating() {
        final Optional<Timer.Context> timer = MetricManager.startTimer(className, "listMovieWithoutRottenRating");
        try {
            return statusRepository.listMovieWithoutRottenTomatoesRating();
        } finally {
            MetricManager.stopTimer(timer);

        }
    }

    public List<Movie> listMovieWithoutTrackTv() {
        final Optional<Timer.Context> timer = MetricManager.startTimer(className, "listMovieWithoutTrackTv");
        try {
            return statusRepository.listMovieWithoutTrackTvInformation();
        } finally {
            MetricManager.stopTimer(timer);

        }
    }
}
