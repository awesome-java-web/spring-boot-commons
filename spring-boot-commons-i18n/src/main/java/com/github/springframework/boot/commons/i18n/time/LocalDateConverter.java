package com.github.springframework.boot.commons.i18n.time;

import com.github.springframework.boot.commons.i18n.util.DateTimeUtils;

import java.time.LocalDate;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;

public class LocalDateConverter implements ZonedDateTimeConverter<LocalDate> {

	@Override
	public LocalDate convert(LocalDate datetime, ZoneId targetZoneId) {
		return DateTimeUtils.toLocalDate(datetime, targetZoneId);
	}

	@Override
	public DateTimeFormatter formatterOf(LocalDate datetime) {
		return DateTimeFormatter.ISO_LOCAL_DATE;
	}

}
