package com.ninjas.movietime.core.domain.exception;

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

    public CannotFindIMDIdException(String message, String movieId, String movieTitle) {
        super(message);
        this.movieId = movieId;
        this.movieTitle = movieTitle;
    }
}
