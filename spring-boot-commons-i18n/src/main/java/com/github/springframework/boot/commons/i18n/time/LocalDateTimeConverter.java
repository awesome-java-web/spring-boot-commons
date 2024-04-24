package com.github.springframework.boot.commons.i18n.time;

import com.github.springframework.boot.commons.i18n.util.DateTimeUtils;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;

public class LocalDateTimeConverter implements ZonedDateTimeConverter<LocalDateTime> {

	@Override
	public LocalDateTime convert(LocalDateTime datetime, ZoneId targetZoneId) {
		return DateTimeUtils.toLocalDateTime(datetime, targetZoneId);
	}

	@Override
	public DateTimeFormatter formatterOf(LocalDateTime datetime) {
		return DateTimeFormatter.ofPattern(DateTimeUtils.DEFAULT_DATE_TIME_PATTERN);
	}

}
