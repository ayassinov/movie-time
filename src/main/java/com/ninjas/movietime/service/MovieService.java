package com.ninjas.movietime.service;

import com.ninjas.movietime.repository.MovieRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author ayassinov on 29/08/2014.
 */
@Service
public class MovieService {

    private final MovieRepository movieRepository;

    @Autowired
    public MovieService(MovieRepository movieRepository) {
        this.movieRepository = movieRepository;
    }

    public List<String> findDistinctGenre() {
        return this.movieRepository.genresName();
    }

}
