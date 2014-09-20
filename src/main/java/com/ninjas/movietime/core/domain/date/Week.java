package com.ninjas.movietime.core.domain.date;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.Date;

/**
 * @author ayassinov on 20/09/14.
 */
@Setter
@Getter
@ToString
public class Week {

    private Date monday;
    private Date sunday;
}
