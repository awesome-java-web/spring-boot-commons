package com.github.springframework.boot.commons.mybatis.converter;

import com.github.springframework.boot.commons.mybatis.util.DateTimeUtils;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Date;

public interface ZonedDateTimeConverter {

    default boolean isParseableDateTime(final String datetime) {
        return DateTimeUtils.isParseableLocalDateTime(datetime) || DateTimeUtils.isParseableLocalDate(datetime);
    }

    default DateTimeFormatter formatterOf(final String datetime) {
        if (DateTimeUtils.isParseableLocalDateTime(datetime)) {
            return DateTimeFormatter.ofPattern(DateTimeUtils.DEFAULT_DATE_TIME_PATTERN);
        } else if (DateTimeUtils.isParseableLocalDate(datetime)) {
            return DateTimeFormatter.ISO_LOCAL_DATE;
        }
        return DateTimeFormatter.ofPattern(DateTimeUtils.DEFAULT_DATE_TIME_PATTERN);
    }

    default Object convert(Object value, ZoneId targetZoneId) {
        if (value instanceof String && isParseableDateTime(value.toString())) {
            DateTimeFormatter formatter = formatterOf(value.toString());
            LocalDateTime localDateTime = DateTimeUtils.toLocalDateTime(value.toString(), formatter, targetZoneId);
            return DateTimeUtils.stringify(localDateTime, formatter);
        } else if (value instanceof Date) {
            LocalDateTime localDateTime = DateTimeUtils.toLocalDateTime((Date) value, targetZoneId);
            return DateTimeUtils.toDate(localDateTime);
        } else if (value instanceof LocalDate) {
            return DateTimeUtils.toLocalDate((LocalDate) value, targetZoneId);
        } else if (value instanceof LocalDateTime) {
            return DateTimeUtils.toLocalDateTime((LocalDateTime) value, targetZoneId);
        }
        return value;
    }

}
