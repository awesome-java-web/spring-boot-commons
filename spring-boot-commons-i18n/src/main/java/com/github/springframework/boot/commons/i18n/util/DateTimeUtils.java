package com.github.springframework.boot.commons.i18n.util;

import java.time.*;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.Date;

public final class DateTimeUtils {

    public static final String DEFAULT_DATE_TIME_PATTERN = "yyyy-MM-dd HH:mm:ss";

    DateTimeUtils() {
        throw new UnsupportedOperationException("Utility class should not be instantiated");
    }

    public static LocalDateTime toLocalDateTime(final String datetime, DateTimeFormatter formatter, ZoneId targetZoneId) {
        LocalDateTime localDateTime = LocalDateTime.parse(datetime, formatter);
        return toLocalDateTime(localDateTime, targetZoneId);
    }

    public static LocalDateTime toLocalDateTime(Date date, ZoneId targetZoneId) {
        ZonedDateTime zonedDateTime = date.toInstant().atZone(ZoneId.systemDefault());
        return zonedDateTime.withZoneSameInstant(targetZoneId).toLocalDateTime();
    }

    public static LocalDateTime toLocalDateTime(LocalDateTime localDateTime, ZoneId targetZoneId) {
        ZonedDateTime zonedDateTime = localDateTime.atZone(ZoneId.systemDefault());
        return zonedDateTime.withZoneSameInstant(targetZoneId).toLocalDateTime();
    }

    public static LocalDate toLocalDate(final String date, DateTimeFormatter formatter, ZoneId targetZoneId) {
        LocalDate localDate = LocalDate.parse(date, formatter);
        return toLocalDate(localDate, targetZoneId);
    }

    public static LocalDate toLocalDate(LocalDate localDate, ZoneId targetZoneId) {
        ZonedDateTime zonedDateTime = localDate.atStartOfDay(ZoneId.systemDefault());
        return zonedDateTime.withZoneSameInstant(targetZoneId).toLocalDate();
    }

    public static Date toDate(LocalDateTime localDateTime) {
        return Date.from(localDateTime.atZone(ZoneId.systemDefault()).toInstant());
    }

    public static boolean isParseableLocalDateTime(final String datetime) {
        try {
            LocalDateTime.parse(datetime, DateTimeFormatter.ofPattern(DEFAULT_DATE_TIME_PATTERN));
            return true;
        } catch (DateTimeParseException e) {
            return false;
        }
    }

    public static boolean isParseableLocalDate(final String date) {
        try {
            LocalDate.parse(date, DateTimeFormatter.ISO_LOCAL_DATE);
            return true;
        } catch (DateTimeParseException e) {
            return false;
        }
    }

    public static boolean isSameZone(ZoneId zoneId, ZoneId anotherZoneId) {
        LocalDateTime now = LocalDateTime.now();
        Instant instant = now.atZone(zoneId).toInstant();
        Instant anotherInstant = now.atZone(anotherZoneId).toInstant();
        return instant.equals(anotherInstant);
    }

    public static LocalDateTime max(LocalDateTime... datetime) {
        LocalDateTime max = datetime[0];
        for (LocalDateTime dt : datetime) {
            max = dt.isAfter(max) ? dt : max;
        }
        return max;
    }

    public static LocalDateTime min(LocalDateTime... datetime) {
        LocalDateTime min = datetime[0];
        for (LocalDateTime dt : datetime) {
            min = dt.isBefore(min) ? dt : min;
        }
        return min;
    }

    public static LocalDate max(LocalDate... date) {
        LocalDate max = date[0];
        for (LocalDate dt : date) {
            max = dt.isAfter(max) ? dt : max;
        }
        return max;
    }

    public static LocalDate min(LocalDate... date) {
        LocalDate min = date[0];
        for (LocalDate dt : date) {
            min = dt.isBefore(min) ? dt : min;
        }
        return min;
    }

}
