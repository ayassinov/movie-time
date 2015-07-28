package com.ninjas.movietime.core.domain.movie;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.joda.time.DateTime;

/**
 * @author ayassinov on 10/09/14.
 */
@Getter
@Setter
@ToString
public class MovieUpdateStatus {

    private DateTime lastAlloCineUpdate;

    private DateTime lastRottenTomatoesUpdate;

    private DateTime lastTrackTvUpdate;

    private DateTime lastImdbUpdate;

    private boolean isAlloCineFullUpdated;

}
