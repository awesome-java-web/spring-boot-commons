package com.github.springframework.boot.commons.i18n.util;

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
    void testToLocalDateTime(String datetimeWithDefaultZone, String targetZoneId, String expected) throws ParseException {
        ZoneId zoneId = ZoneId.of(targetZoneId);
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern(DateTimeUtils.DEFAULT_DATE_TIME_PATTERN);

        LocalDateTime result = DateTimeUtils.toLocalDateTime(datetimeWithDefaultZone, formatter, zoneId);
        assertEquals(expected, result.format(formatter));

        SimpleDateFormat dateFormat = new SimpleDateFormat(DateTimeUtils.DEFAULT_DATE_TIME_PATTERN);
        Date parsedDate = dateFormat.parse(datetimeWithDefaultZone);
        result = DateTimeUtils.toLocalDateTime(parsedDate, zoneId);
        assertEquals(expected, result.format(formatter));

        LocalDateTime localDateTime = LocalDateTime.parse(datetimeWithDefaultZone, formatter);
        result = DateTimeUtils.toLocalDateTime(localDateTime, zoneId);
        assertEquals(expected, result.format(formatter));

        LocalDate localDate = localDateTime.toLocalDate();
        LocalDate zonedLocalDate = DateTimeUtils.toLocalDate(localDate, zoneId);
        assertEquals(expected.substring(0, 10), zonedLocalDate.toString());

        Date date = DateTimeUtils.toDate(localDateTime);
        result = DateTimeUtils.toLocalDateTime(date, zoneId);
        assertEquals(expected, result.format(formatter));
    }

    @ParameterizedTest
    @CsvSource({"2022-11-07, Asia/Phnom_Penh, 2022-11-06", "2022-11-07, America/New_York, 2022-11-06"})
    void testToLocalDate(String datetimeWithDefaultZone, String targetZoneId, String expected) {
        ZoneId zoneId = ZoneId.of(targetZoneId);
        DateTimeFormatter formatter = DateTimeFormatter.ISO_LOCAL_DATE;
        LocalDate localDate = DateTimeUtils.toLocalDate(datetimeWithDefaultZone, formatter, zoneId);
        assertEquals(expected, localDate.format(formatter));
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
        assertEquals(expected, DateTimeUtils.isSameZone(ZoneId.of(zoneId), ZoneId.of(anotherZoneId)));
    }

    @Test
    void testMinAndMaxLocalDateTime() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern(DateTimeUtils.DEFAULT_DATE_TIME_PATTERN);
        LocalDateTime datetime1 = LocalDateTime.parse("2024-11-07 00:00:00", formatter);
        LocalDateTime datetime2 = LocalDateTime.parse("2024-11-06 23:00:00", formatter);
        LocalDateTime datetime3 = LocalDateTime.parse("2023-11-07 00:00:00", formatter);
        LocalDateTime datetime4 = LocalDateTime.parse("2023-11-06 23:00:00", formatter);

        LocalDateTime max = DateTimeUtils.max(datetime1, datetime2, datetime3, datetime4);
        assertEquals(max, datetime1);

        LocalDateTime min = DateTimeUtils.min(datetime1, datetime2, datetime3, datetime4);
        assertEquals(min, datetime4);
    }

    @Test
    void testMinAndMaxLocalDate() {
        LocalDate date1 = LocalDate.parse("2024-11-07");
        LocalDate date2 = LocalDate.parse("2024-11-06");
        LocalDate date3 = LocalDate.parse("2023-11-07");
        LocalDate date4 = LocalDate.parse("2023-11-06");

        LocalDate max = DateTimeUtils.max(date1, date2, date3, date4);
        assertEquals(max, date1);

        LocalDate min = DateTimeUtils.min(date1, date2, date3, date4);
        assertEquals(min, date4);
    }

}