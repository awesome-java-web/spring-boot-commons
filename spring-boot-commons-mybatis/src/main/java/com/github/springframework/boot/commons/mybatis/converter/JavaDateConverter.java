package com.github.springframework.boot.commons.mybatis.converter;

import com.github.springframework.boot.commons.mybatis.util.DateTimeUtils;

import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Date;

public class JavaDateConverter implements ZonedDateTimeConverter<Date> {

	@Override
	public Date convert(Date datetime, ZoneId targetZoneId) {
		return DateTimeUtils.toDate(DateTimeUtils.toLocalDateTime(datetime, targetZoneId));
	}

	@Override
	public DateTimeFormatter formatterOf(Date datetime) {
		return DateTimeFormatter.ofPattern(DateTimeUtils.DEFAULT_DATE_TIME_PATTERN);
	}

}
