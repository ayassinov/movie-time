package com.ninjas.movietime.core.domain.showtime;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.EqualsAndHashCode;
import lombok.Getter;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * @author ayassinov on 29/08/2014.
 */
@Getter
@EqualsAndHashCode(of = {"time", "timeZone"})
public class Time {

    public static final SimpleDateFormat TIME_FORMAT = new SimpleDateFormat("HH:mm");

    @JsonIgnore
    private final Date time;

    private final String timeZone;

    public Time(Date time) {
        this.time = time;
        this.timeZone = "GMT+2";
    }

    @JsonProperty("time")
    public String getTime() {
        return TIME_FORMAT.format(time);
    }

    @Override
    public String toString() {
        return "Time{" +
                "time=" + getTime() +
                '}';
    }
}
