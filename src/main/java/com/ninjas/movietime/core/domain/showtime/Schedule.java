package com.ninjas.movietime.core.domain.showtime;

import lombok.Data;
import lombok.ToString;

import java.util.*;

/**
 * @author ayassinov on 28/08/2014.
 */
@Data
@ToString
public class Schedule {

    private List<DateAndTime> dateAndTimes = new ArrayList<>();

    private ScreenFormat screenFormat;

    private Language language;

    private boolean isVO;

    public Schedule() {
    }

    public void addDateAndTime(DateAndTime dateAndTime) {
        this.dateAndTimes.add(dateAndTime);
    }

    public void addDateAndTime(Collection<DateAndTime> dateAndTimes) {
        if (this.dateAndTimes.size() > 0)
            throw new IllegalArgumentException("Cann't add a list of DateAndTime to not empty existing list");

        this.dateAndTimes.addAll(dateAndTimes);
    }

    @Data
    public static class ScreenFormat {
        private String code;
        private String name;

        public ScreenFormat(String code, String name) {
            this.code = code;
            this.name = name;
        }
    }

    @Data
    public static class Language {
        private String code;
        private String name;

        public Language(String code, String name) {
            this.code = code;
            this.name = name;
        }
    }
}
