package com.ninjas.movietime.repository;

import com.ninjas.movietime.core.domain.movie.Movie;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @author ayassinov on 29/08/2014.
 */
@Repository
public interface MovieRepository extends MongoRepository<Movie, String> {

    List<String> genresName();

}
