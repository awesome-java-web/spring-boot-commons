package com.github.springframework.boot.commons.util.time;

import java.time.*;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.Date;

public final class DateTime {

    public static final String DEFAULT_DATE_TIME_PATTERN = "yyyy-MM-dd HH:mm:ss";

    public static final DateTimeFormatter DEFAULT_DATE_FORMATTER = DateTimeFormatter.ISO_LOCAL_DATE;

    public static final DateTimeFormatter DEFAULT_DATE_TIME_FORMATTER = DateTimeFormatter.ofPattern(DEFAULT_DATE_TIME_PATTERN);

    public DateTime() {
        throw new UnsupportedOperationException("Utility class should not be instantiated");
    }

    public static Object atZone(Object datetime, ZoneId targetZoneId) {
        if (datetime == null || targetZoneId == null) {
            throw new IllegalArgumentException("datetime or targetZoneId is null");
        }

        if (datetime instanceof Date) {
            return toDate(toLocalDateTime((Date) datetime, targetZoneId));
        }

        if (datetime instanceof String) {
            final String dt = datetime.toString();
            final boolean isLocalDate = isParseableLocalDate(dt);
            DateTimeFormatter formatter = isLocalDate ? DEFAULT_DATE_FORMATTER : DEFAULT_DATE_TIME_FORMATTER;
            return isLocalDate
                ? toLocalDate(dt, formatter, targetZoneId).format(formatter)
                : toLocalDateTime(dt, formatter, targetZoneId).format(formatter);
        }

        if (datetime instanceof LocalDate) {
            return toLocalDate((LocalDate) datetime, targetZoneId);
        }

        if (datetime instanceof LocalDateTime) {
            return toLocalDateTime((LocalDateTime) datetime, targetZoneId);
        }

        throw new IllegalArgumentException("Unsupported type: " + datetime.getClass().getName());
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
