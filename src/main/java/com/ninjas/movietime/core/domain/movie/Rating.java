package com.ninjas.movietime.core.domain.movie;

import lombok.Data;

/**
 * @author ayassinov on 28/08/2014.
 */
@Data
public class Rating {

    private double pressRating;
    private double userRating;

    private double imdbRating;
    private int imdbVoteCount;

    private double rottenCriticsRating;
    private double rottenUserRating;

    private double trackTvRating;
    private double trackTvVoteCount;

    public Rating() {
    }

    public Rating(double pressRating, double userRating) {
        this.pressRating = pressRating;
        this.userRating = userRating;
    }
}
