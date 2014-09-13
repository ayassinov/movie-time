package com.ninjas.movietime.integration.exception;

import lombok.Getter;
import lombok.ToString;

/**
 * @author ayassinov on 04/09/14.
 */
@Getter
@ToString
public class CannotFindIMDIdException extends RuntimeException {

    private String movieId;
    private String movieTitle;

    public CannotFindIMDIdException() {
    }

    public CannotFindIMDIdException(String movieId, String movieTitle) {
        this.movieId = movieId;
        this.movieTitle = movieTitle;
    }

    public CannotFindIMDIdException(String message, String movieId, String movieTitle) {
        super(String.format(message, movieId, movieTitle));
        this.movieId = movieId;
        this.movieTitle = movieTitle;
    }
}
