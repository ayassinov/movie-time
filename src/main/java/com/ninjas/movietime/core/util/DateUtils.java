package com.ninjas.movietime.core.util;

import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;
import org.joda.time.format.DateTimeFormat;
import org.springframework.scheduling.support.CronSequenceGenerator;

import java.util.Calendar;
import java.util.Date;
import java.util.SimpleTimeZone;
import java.util.TimeZone;

/**
 * @author ayassinov on 26/08/2014.
 */
public final class DateUtils {

    public final static TimeZone SERVER_TIME_ZONE = TimeZone.getDefault();
    public final static TimeZone PARIS_TIME_ZONE = new SimpleTimeZone(3600000,
            "Europe/Paris",
            Calendar.MARCH, -1, Calendar.SUNDAY,
            3600000, SimpleTimeZone.UTC_TIME,
            Calendar.OCTOBER, -1, Calendar.SUNDAY,
            3600000, SimpleTimeZone.UTC_TIME,
            3600000);

    private DateUtils() {
    }

    public static boolean isAfterNow(Date thatDate) {
        if (thatDate == null)
            return false;
        final DateTime thatDateTime = new DateTime(thatDate);
        return thatDateTime.isAfterNow();
    }

    public static boolean isDateBeforeNow(Date thatDate) {
        if (thatDate == null)
            return false;
        final DateTime thatDateTime = new DateTime(thatDate).withTime(0, 0, 0, 0);
        final DateTime jodaNow = nowServerDateTime().withTime(0, 0, 0, 0);
        return thatDateTime.isBefore(jodaNow);
    }

    public static Date nowServerDate() {
        return DateTime.now(DateTimeZone.forTimeZone(SERVER_TIME_ZONE)).toDate();
    }

    public static DateTime nowServerDateTime() {
        return DateTime.now(DateTimeZone.forTimeZone(SERVER_TIME_ZONE));
    }

    public static DateTime nowParisDateTime() {
        return DateTime.now(DateTimeZone.forTimeZone(PARIS_TIME_ZONE));
    }

    public static Date parse(String date) {
        if (StringUtils.isNullOrEmpty(date))
            return null;
        return DateTime.parse(date).toDate();
    }

    public static Date parse(String date, String format) {
        return parseDateTime(date, format).toDate();
    }

    public static DateTime parseDateTime(String date, String format){
        if (StringUtils.isNullOrEmpty(date))
            return null;
        return DateTime.parse(date, DateTimeFormat.forPattern(format)).withZoneRetainFields(DateTimeZone.forTimeZone(PARIS_TIME_ZONE));
    }

    public static int getCurrentYear() {
        return DateTime.now().getYear();
    }

    public static Date nextCronStartDate(String cronExpression) {
        final CronSequenceGenerator cronSequenceGenerator = new CronSequenceGenerator(cronExpression, SERVER_TIME_ZONE);
        return cronSequenceGenerator.next(DateUtils.nowServerDate());
    }


}
