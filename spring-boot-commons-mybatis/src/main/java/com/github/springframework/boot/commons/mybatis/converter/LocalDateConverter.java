package com.github.springframework.boot.commons.mybatis.converter;

import com.github.springframework.boot.commons.mybatis.util.DateTimeUtils;

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
