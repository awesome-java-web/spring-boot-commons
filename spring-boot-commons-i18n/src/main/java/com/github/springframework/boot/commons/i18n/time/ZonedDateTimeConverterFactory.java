package com.github.springframework.boot.commons.i18n.time;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;

public final class ZonedDateTimeConverterFactory {

	ZonedDateTimeConverterFactory() {
		throw new UnsupportedOperationException("Factory class should not be instantiated");
	}

	public static Object convert(Object datetime, ZoneId targetZoneId) {
		if (datetime == null) {
			return null;
		}

		if (datetime instanceof Date) {
			JavaDateConverter converter = new JavaDateConverter();
			return converter.convert((Date) datetime, targetZoneId);
		} else if (datetime instanceof String) {
			StringDateTimeConverter converter = new StringDateTimeConverter();
			return converter.convert(datetime.toString(), targetZoneId);
		} else if (datetime instanceof LocalDate) {
			LocalDateConverter localDateConverter = new LocalDateConverter();
			return localDateConverter.convert((LocalDate) datetime, targetZoneId);
		} else if (datetime instanceof LocalDateTime) {
			LocalDateTimeConverter localDateTimeConverter = new LocalDateTimeConverter();
			return localDateTimeConverter.convert((LocalDateTime) datetime, targetZoneId);
		}

		throw new UnsupportedOperationException("No suitable ZonedDateTimeConverter found to handle this data type: "
				+ datetime.getClass() + ", you can report a issue on GitHub or consider to create a custom"
				+ "ZonedDateTimeConverter implementation to support it.");
	}

}
