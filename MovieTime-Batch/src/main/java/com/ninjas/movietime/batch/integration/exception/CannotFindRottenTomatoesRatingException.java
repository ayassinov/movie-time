package com.ninjas.movietime.batch.integration.exception;

import lombok.Getter;
import lombok.ToString;

/**
 * @author ayassinov on 04/09/14.
 */
@Getter
@ToString
public class CannotFindRottenTomatoesRatingException extends RuntimeException {
    private String movieId;
    private String imdbId;
    private String movieTitle;

    public CannotFindRottenTomatoesRatingException() {
    }

    public CannotFindRottenTomatoesRatingException(String message, String movieId, String imdbId, String movieTitle) {
        super(message);
        this.movieId = movieId;
        this.imdbId = imdbId;
        this.movieTitle = movieTitle;
    }

}
