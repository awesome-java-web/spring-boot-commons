package com.github.springframework.boot.commons.mybatis.converter;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Date;

public final class ZonedDateTimeConverterFactory {

	@SuppressWarnings("rawtypes")
	public static ZonedDateTimeConverter getConverter(Object datetime) {
		if (datetime instanceof Date) {
			return new JavaDateConverter();
		} else if (datetime instanceof String) {
			return new StringDateTimeConverter();
		} else if (datetime instanceof LocalDate) {
			return new LocalDateConverter();
		} else if (datetime instanceof LocalDateTime) {
			return new LocalDateTimeConverter();
		}
		throw new UnsupportedOperationException("No suitable ZonedDateTimeConverter found to handle this data type: "
				+ datetime.getClass() + ", you can report a issue on GitHub or consider to create a custom"
				+ "ZonedDateTimeConverter implementation to support it.");
	}

}
