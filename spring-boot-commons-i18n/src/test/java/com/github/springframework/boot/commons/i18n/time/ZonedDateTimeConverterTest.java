package com.github.springframework.boot.commons.i18n.time;

import com.github.springframework.boot.commons.i18n.util.DateTimeUtils;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.TimeZone;
import java.util.concurrent.TimeUnit;

import static org.junit.jupiter.api.Assertions.*;

class ZonedDateTimeConverterTest {

	static ZoneId targetZoneId;

	@BeforeAll
	static void setup() {
		TimeZone.setDefault(TimeZone.getTimeZone("Asia/Shanghai"));
		targetZoneId = ZoneId.of("Asia/Phnom_Penh");
	}

	@Test
	void testZonedDateTimeConverterFactoryNewInstance() {
		UnsupportedOperationException e = assertThrows(UnsupportedOperationException.class, ZonedDateTimeConverterFactory::new);
		assertEquals("Factory class should not be instantiated", e.getMessage());
	}

	@Test
	void testConvertDate() {
		Date now = new Date();
		Date converted = (Date) ZonedDateTimeConverterFactory.convert(now, targetZoneId);
		assertEquals(TimeUnit.HOURS.toMillis(1), now.getTime() - converted.getTime());

		JavaDateConverter converter = new JavaDateConverter();
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern(DateTimeUtils.DEFAULT_DATE_TIME_PATTERN);
		assertEquals(formatter.toString(), converter.formatterOf(now).toString());
	}

	@Test
	void testConvertString() {
		assertNull(ZonedDateTimeConverterFactory.convert(null, targetZoneId));

		final String datetime = "2022-11-07 00:00:00";
		String converted = ZonedDateTimeConverterFactory.convert(datetime, targetZoneId).toString();
		assertEquals("2022-11-06 23:00:00", converted);

		final String date = "2022-11-07";
		converted = ZonedDateTimeConverterFactory.convert(date, targetZoneId).toString();
		assertEquals("2022-11-06", converted);

		final String NonDatetime = "This is not a datetime string";
		converted = ZonedDateTimeConverterFactory.convert(NonDatetime, targetZoneId).toString();
		assertEquals(NonDatetime, converted);
	}

	@Test
	void testConvertLocalDate() {
		LocalDate now = LocalDate.now();
		LocalDate converted = (LocalDate) ZonedDateTimeConverterFactory.convert(now, targetZoneId);
		assertEquals(now.getYear(), converted.getYear());
		final int diffMonthValue = now.getMonthValue() - converted.getMonthValue();
		assertTrue(diffMonthValue == 0 || diffMonthValue == 1);
		final int diffDays = now.getDayOfMonth() - converted.getDayOfMonth();
		assertTrue(diffDays == 0 || diffDays == 1 || diffDays == -30 || diffDays == -29 || diffDays == -28 || diffDays == -27);

		LocalDateConverter converter = new LocalDateConverter();
		assertEquals(DateTimeFormatter.ISO_LOCAL_DATE.toString(), converter.formatterOf(now).toString());
	}

	@Test
	void testConvertLocalDateTime() {
		LocalDateTime now = LocalDateTime.now();
		LocalDateTime converted = (LocalDateTime) ZonedDateTimeConverterFactory.convert(now, targetZoneId);
		assertEquals(now.getYear(), converted.getYear());
		final int diffMonthValue = now.getMonthValue() - converted.getMonthValue();
		assertTrue(diffMonthValue == 0 || diffMonthValue == 1);
		final int diffDays = now.getDayOfMonth() - converted.getDayOfMonth();
		assertTrue(diffDays == 0 || diffDays == 1 || diffDays == -30 || diffDays == -29 || diffDays == -28 || diffDays == -27);
		final int diffHours = now.getHour() - converted.getHour();
		assertTrue(diffHours == 1 || diffHours == -23);
		assertEquals(now.getMinute(), converted.getMinute());
		assertEquals(now.getSecond(), converted.getSecond());

		LocalDateTimeConverter converter = new LocalDateTimeConverter();
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern(DateTimeUtils.DEFAULT_DATE_TIME_PATTERN);
		assertEquals(formatter.toString(), converter.formatterOf(now).toString());
	}

	@Test
	void testUnsupportedDataType() {
		UnsupportedOperationException e = assertThrows(UnsupportedOperationException.class,
			() -> ZonedDateTimeConverterFactory.convert(Integer.MIN_VALUE, targetZoneId)
		);
		assertTrue(e.getMessage().contains("No suitable ZonedDateTimeConverter found to handle this data type"));
	}

}
