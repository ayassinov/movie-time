package com.ninjas.movietime.core.util;

import com.ninjas.movietime.core.domain.date.Week;
import org.joda.time.DateTime;
import org.joda.time.DateTimeConstants;
import org.joda.time.DateTimeZone;
import org.joda.time.LocalDate;
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

    public static boolean isDateBeforeNow(DateTime thatDate) {
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

    public static Date parse(String date, String format) {
        return parseDateTime(date, format).toDate();
    }

    public static DateTime parseDateTime(String date) {
        return parseDateTime(date, null);
    }

    public static DateTime parseDateTime(String date, String format) {
        if (StringUtils.isNullOrEmpty(date))
            return null;

        final DateTime dateTime;
        if (format == null)
            dateTime = DateTime.parse(date);
        else
            dateTime = DateTime.parse(date, DateTimeFormat.forPattern(format));

        return dateTime.withZoneRetainFields(DateTimeZone.forTimeZone(PARIS_TIME_ZONE));
    }

    public static Date nextCronStartDate(String cronExpression) {
        final CronSequenceGenerator cronSequenceGenerator = new CronSequenceGenerator(cronExpression, SERVER_TIME_ZONE);
        return cronSequenceGenerator.next(DateUtils.nowServerDate());
    }

    public static Week getCurrentWeek() {
        final LocalDate now = new LocalDate(DateTimeZone.forTimeZone(SERVER_TIME_ZONE));
        final Week week = new Week();
        week.setMonday(now.withDayOfWeek(DateTimeConstants.MONDAY).toDate());
        week.setSunday(now.withDayOfWeek(DateTimeConstants.SUNDAY).toDate());
        return week;
    }

}
