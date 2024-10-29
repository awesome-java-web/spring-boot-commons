package com.github.springframework.boot.commons.i18n.time;

import com.github.springframework.boot.commons.i18n.util.DateTimeUtils;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;

public class StringDateTimeConverter implements ZonedDateTimeConverter<String> {

	@Override
	public String convert(String datetime, ZoneId targetZoneId) {
		DateTimeFormatter formatter = formatterOf(datetime);
		if (DateTimeUtils.isParseableLocalDateTime(datetime)) {
			LocalDateTime localDateTime = DateTimeUtils.toLocalDateTime(datetime, formatter, targetZoneId);
			return localDateTime.format(formatter);
		} else if (DateTimeUtils.isParseableLocalDate(datetime)) {
			LocalDate localDate = DateTimeUtils.toLocalDate(datetime, formatter, targetZoneId);
			return localDate.format(formatter);
		}
		// Ignore if it is not a parseable string in datetime format
		return datetime;
	}

	@Override
	public DateTimeFormatter formatterOf(String datetime) {
		if (DateTimeUtils.isParseableLocalDate(datetime)) {
			return DateTimeFormatter.ISO_LOCAL_DATE;
		}
		return DateTimeFormatter.ofPattern(DateTimeUtils.DEFAULT_DATE_TIME_PATTERN);
	}

}
