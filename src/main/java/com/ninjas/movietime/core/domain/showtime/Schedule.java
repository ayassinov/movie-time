package com.ninjas.movietime.core.domain.showtime;

import lombok.Data;
import lombok.ToString;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * @author ayassinov on 28/08/2014.
 */
@Data
@ToString
public class Schedule {

   /* public static final SimpleDateFormat DATE_FORMAT = new SimpleDateFormat("dd/MM/yyyy");
    public static final SimpleDateFormat TIME_FORMAT = new SimpleDateFormat("HH:mm");*/

    private List<Date> dates = new ArrayList<>();

    // private LanguageEnum language;

    //private ScreenFormatEnum screenFormat;

    private ScreenFormat screenFormat;

    private Language language;

    private boolean isVO;

    public Schedule() {
    }

    public void addDateTime(Date date) {
        dates.add(date);
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
