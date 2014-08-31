package com.ninjas.movietime.core.util;

import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;
import org.joda.time.format.DateTimeFormat;

import java.util.Date;

/**
 * @author ayassinov on 26/08/2014.
 */
public final class DateUtils {

    private final static DateTimeZone DEFAULT_TIME_ZONE = DateTimeZone.forID("UTC");

    private DateUtils() {
    }

    public static boolean isAfterNow(Date thatDate) {
        if (thatDate == null)
            return false;
        final DateTime thatDateTime = new DateTime(thatDate);
        return thatDateTime.isAfterNow();
    }

    public static boolean isBeforeNow(Date thatDate) {
        if (thatDate == null)
            return false;
        final DateTime thatDateTime = new DateTime(thatDate);
        return thatDateTime.isBeforeNow();
    }

    public static Date now() {
        return DateTime.now().toDate();
    }

    public static Date parse(String date) {
        if (StringUtils.isNullOrEmpty(date))
            return null;
        return DateTime.parse(date).toDate();
    }

    public static Date parse(String date, String format) {
        if (StringUtils.isNullOrEmpty(date))
            return null;
        return DateTime.parse(date, DateTimeFormat.forPattern(format)).toDate();
    }

    public static int getCurrentYear() {
        return DateTime.now().getYear();
    }
}
