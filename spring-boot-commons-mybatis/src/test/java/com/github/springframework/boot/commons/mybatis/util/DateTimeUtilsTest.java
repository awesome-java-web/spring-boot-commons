package com.github.springframework.boot.commons.mybatis.util;

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

class DateTimeUtilsTest {

    @BeforeAll
    static void setup() {
		TimeZone.setDefault(TimeZone.getTimeZone("Asia/Shanghai"));
    }

    @Test
    void testNewInstance() {
        UnsupportedOperationException e = assertThrows(UnsupportedOperationException.class, DateTimeUtils::new);
        assertEquals("Utility class should not be instantiated", e.getMessage());
    }

    @ParameterizedTest
    @CsvSource({
			"2022-11-07 00:00:00, Asia/Phnom_Penh, 2022-11-06 23:00:00",
			"2022-11-07 00:00:00, America/New_York, 2022-11-06 11:00:00"
	})
    void testToLocalDateTime(String datetimeAsDefaultZoneId, String targetZoneId, String expected) throws ParseException {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern(DateTimeUtils.DEFAULT_DATE_TIME_PATTERN);
        ZoneId zoneId = ZoneId.of(targetZoneId);

        LocalDateTime result = DateTimeUtils.toLocalDateTime(datetimeAsDefaultZoneId, formatter, zoneId);
        assertEquals(expected, DateTimeUtils.stringify(result, formatter));

        SimpleDateFormat sdf = new SimpleDateFormat(DateTimeUtils.DEFAULT_DATE_TIME_PATTERN);
        Date parsedDate = sdf.parse(datetimeAsDefaultZoneId);
        result = DateTimeUtils.toLocalDateTime(parsedDate, zoneId);
        assertEquals(expected, DateTimeUtils.stringify(result, formatter));

        LocalDateTime localDateTime = LocalDateTime.parse(datetimeAsDefaultZoneId, formatter);
        result = DateTimeUtils.toLocalDateTime(localDateTime, zoneId);
        assertEquals(expected, DateTimeUtils.stringify(result, formatter));

        LocalDate localDate = localDateTime.toLocalDate();
        LocalDate zonedLocalDate = DateTimeUtils.toLocalDate(localDate, zoneId);
        assertEquals(expected.substring(0, 10), zonedLocalDate.toString());

        Date date = DateTimeUtils.toDate(localDateTime);
        result = DateTimeUtils.toLocalDateTime(date, zoneId);
        assertEquals(expected, DateTimeUtils.stringify(result, formatter));
    }

    @ParameterizedTest
    @CsvSource({"2022-11-07 00:00:00, true", "This is not a LocalDateTime string, false"})
    void testIsParseableLocalDateTime(String datetime, boolean expected) {
        assertEquals(expected, DateTimeUtils.isParseableLocalDateTime(datetime));
    }

    @ParameterizedTest
    @CsvSource({"2022-11-07, true", "This is not a LocalDate string, false"})
    void testIsParseableLocalDate(String date, boolean expected) {
        assertEquals(expected, DateTimeUtils.isParseableLocalDate(date));
    }

}