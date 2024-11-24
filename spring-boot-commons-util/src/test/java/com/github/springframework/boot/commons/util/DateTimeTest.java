package com.github.springframework.boot.commons.util;

import com.github.springframework.boot.commons.util.time.DateTime;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.TimeZone;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class DateTimeTest {

	@BeforeAll
	static void setup() {
		TimeZone.setDefault(TimeZone.getTimeZone("Asia/Shanghai"));
	}

	@Test
	void testNewInstance() {
		UnsupportedOperationException e = assertThrows(UnsupportedOperationException.class, DateTime::new);
		assertEquals("Utility class should not be instantiated", e.getMessage());
	}

	@ParameterizedTest
	@CsvSource({
		"2022-11-07 00:00:00, Asia/Phnom_Penh, 2022-11-06 23:00:00",
		"2022-11-07 00:00:00, America/New_York, 2022-11-06 11:00:00"
	})
	void testToLocalDateTime(String datetimeWithDefaultZone, String targetZoneId, String expected) throws ParseException {
		ZoneId zoneId = ZoneId.of(targetZoneId);
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern(DateTime.DEFAULT_DATE_TIME_PATTERN);

		LocalDateTime result = DateTime.toLocalDateTime(datetimeWithDefaultZone, formatter, zoneId);
		assertEquals(expected, result.format(formatter));

		SimpleDateFormat dateFormat = new SimpleDateFormat(DateTime.DEFAULT_DATE_TIME_PATTERN);
		Date parsedDate = dateFormat.parse(datetimeWithDefaultZone);
		result = DateTime.toLocalDateTime(parsedDate, zoneId);
		assertEquals(expected, result.format(formatter));

		LocalDateTime localDateTime = LocalDateTime.parse(datetimeWithDefaultZone, formatter);
		result = DateTime.toLocalDateTime(localDateTime, zoneId);
		assertEquals(expected, result.format(formatter));

		LocalDate localDate = localDateTime.toLocalDate();
		LocalDate zonedLocalDate = DateTime.toLocalDate(localDate, zoneId);
		assertEquals(expected.substring(0, 10), zonedLocalDate.toString());

		Date date = DateTime.toDate(localDateTime);
		result = DateTime.toLocalDateTime(date, zoneId);
		assertEquals(expected, result.format(formatter));
	}

	@ParameterizedTest
	@CsvSource({"2022-11-07, Asia/Phnom_Penh, 2022-11-06", "2022-11-07, America/New_York, 2022-11-06"})
	void testToLocalDate(String datetimeWithDefaultZone, String targetZoneId, String expected) {
		ZoneId zoneId = ZoneId.of(targetZoneId);
		DateTimeFormatter formatter = DateTimeFormatter.ISO_LOCAL_DATE;
		LocalDate localDate = DateTime.toLocalDate(datetimeWithDefaultZone, formatter, zoneId);
		assertEquals(expected, localDate.format(formatter));
	}

	@ParameterizedTest
	@CsvSource({"2022-11-07 00:00:00, true", "This is not a LocalDateTime string, false"})
	void testIsParseableLocalDateTime(String datetime, boolean expected) {
		assertEquals(expected, DateTime.isParseableLocalDateTime(datetime));
	}

	@ParameterizedTest
	@CsvSource({"2022-11-07, true", "This is not a LocalDate string, false"})
	void testIsParseableLocalDate(String date, boolean expected) {
		assertEquals(expected, DateTime.isParseableLocalDate(date));
	}

	@ParameterizedTest
	@CsvSource({
		"UTC+7, Asia/Jakarta, true",
		"UTC+8, Asia/Jakarta, false",
		"UTC+8, Asia/Makassar, true",
		"UTC+9, Asia/Makassar, false",
		"UTC+8, Asia/Ujung_Pandang, true",
		"UTC+9, Asia/Ujung_Pandang, false"
	})
	void testIsSameZone(String zoneId, String anotherZoneId, boolean expected) {
		assertEquals(expected, DateTime.isSameZone(ZoneId.of(zoneId), ZoneId.of(anotherZoneId)));
	}

	@Test
	void testMinAndMaxLocalDateTime() {
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern(DateTime.DEFAULT_DATE_TIME_PATTERN);
		LocalDateTime datetime1 = LocalDateTime.parse("2024-11-07 00:00:00", formatter);
		LocalDateTime datetime2 = LocalDateTime.parse("2024-11-06 23:00:00", formatter);
		LocalDateTime datetime3 = LocalDateTime.parse("2023-11-07 00:00:00", formatter);
		LocalDateTime datetime4 = LocalDateTime.parse("2023-11-06 23:00:00", formatter);
		LocalDateTime datetime5 = LocalDateTime.parse("2024-11-11 00:00:00", formatter);

		LocalDateTime max = DateTime.max(datetime1, datetime2, datetime3, datetime4, datetime5);
		assertEquals(max, datetime5);

		LocalDateTime min = DateTime.min(datetime1, datetime2, datetime3, datetime4, datetime5);
		assertEquals(min, datetime4);
	}

	@Test
	void testMinAndMaxLocalDate() {
		LocalDate date1 = LocalDate.parse("2024-11-07");
		LocalDate date2 = LocalDate.parse("2024-11-06");
		LocalDate date3 = LocalDate.parse("2023-11-07");
		LocalDate date4 = LocalDate.parse("2023-11-06");
		LocalDate date5 = LocalDate.parse("2024-11-11");

		LocalDate max = DateTime.max(date1, date2, date3, date4, date5);
		assertEquals(max, date5);

		LocalDate min = DateTime.min(date1, date2, date3, date4, date5);
		assertEquals(min, date4);
	}

}
