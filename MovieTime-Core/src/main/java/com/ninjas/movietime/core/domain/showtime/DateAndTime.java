package com.ninjas.movietime.core.domain.showtime;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.ninjas.movietime.core.util.DateUtils;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import org.springframework.data.annotation.PersistenceConstructor;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * @author ayassinov on 29/08/2014.
 */
@Getter
@EqualsAndHashCode(of = "date")
public class DateAndTime {

    public static final SimpleDateFormat DATE_FORMAT = new SimpleDateFormat("dd/MM/yyyy");

    @JsonIgnore
    private final Date date;

    private final List<Time> times;

    @PersistenceConstructor
    public DateAndTime(Date date) {
        this.date = date;
        this.times = new ArrayList<>();
    }

    public DateAndTime(String date, String format) {
        this(DateUtils.parse(date, format));
    }

    public void addTime(String time, String format) {
        addTime(new Time(DateUtils.parse(time, format)));
    }

    public void addTime(Time time) {
        times.add(time);
    }

    @JsonProperty("date")
    public String getDate() {
        return DATE_FORMAT.format(date);
    }

    @Override
    public String toString() {
        return "DateAndTime{" +
                "date=" + getDate() +
                ", times=" + times +
                '}';
    }
}
