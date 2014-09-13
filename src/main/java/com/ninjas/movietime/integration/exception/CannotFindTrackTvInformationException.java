package com.ninjas.movietime.integration.exception;

import lombok.Getter;
import lombok.ToString;

/**
 * @author ayassinov on 04/09/14.
 */
@Getter
@ToString
public class CannotFindTrackTvInformationException extends RuntimeException {

    private String movieId;
    private String tImdbId;
    private String movieTitle;

    public CannotFindTrackTvInformationException() {
    }

    public CannotFindTrackTvInformationException(String message, String movieId, String tImdbId, String movieTitle) {
        super(message);
        this.movieId = movieId;
        this.tImdbId = tImdbId;
        this.movieTitle = movieTitle;
    }
}
