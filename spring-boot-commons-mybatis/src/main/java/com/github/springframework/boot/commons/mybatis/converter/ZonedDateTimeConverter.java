package com.github.springframework.boot.commons.mybatis.converter;

import java.time.ZoneId;
import java.time.format.DateTimeFormatter;

public interface ZonedDateTimeConverter<T> {

	T convert(T datetime, ZoneId targetZoneId);

	DateTimeFormatter formatterOf(T datetime);

}
