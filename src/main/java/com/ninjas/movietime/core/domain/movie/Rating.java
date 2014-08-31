package com.ninjas.movietime.core.domain.movie;

import lombok.Data;

/**
 * @author ayassinov on 28/08/2014.
 */
@Data
public class Rating {

    private double pressRating;
    private int pressVoteCount;

    private double userRating;
    private int userVoteCount;

    private double imdbRating;
    private int imdbVoteCount;

    private double rottenCriticsRating;
    private double rottenUserRating;

    private double trackTvRating;
    private double trackTvVoteCount;

    public Rating() {
    }

    public Rating(double pressRating, int pressVoteCount, double userRating, int userVoteCount) {
        this.pressRating = pressRating;
        this.pressVoteCount = pressVoteCount;
        this.userRating = userRating;
        this.userVoteCount = userVoteCount;
    }
}
