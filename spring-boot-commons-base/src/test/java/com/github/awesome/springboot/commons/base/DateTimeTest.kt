package com.github.awesome.springboot.commons.base

import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import java.text.SimpleDateFormat
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.ZoneId
import java.util.*
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertTrue

class DateTimeTest {

    @BeforeEach
    fun setup() {
        TimeZone.setDefault(TimeZone.getTimeZone("Asia/Shanghai"))
    }

    @Test
    fun testNewInstance() {
        val e = assertThrows<UnsupportedOperationException>() {
            DateTime()
        }
        assertEquals("Utility class should not be instantiated", e.message)
    }

    @Test
    fun testAtZone() {
        // Test null parameter
        val e1 = assertThrows<IllegalArgumentException> {
            DateTime.atZone(null, ZoneId.systemDefault())
        }
        assertEquals("datetime or targetZoneId is null", e1.message)

        val e2 = assertThrows<IllegalArgumentException> {
            DateTime.atZone(LocalDateTime.now(), null)
        }
        assertEquals("datetime or targetZoneId is null", e2.message)

        // Prepare data
        val zonedDateTime = LocalDateTime.of(2024, 11, 7, 0, 0, 0).atZone(ZoneId.systemDefault())
        val targetZoneId = ZoneId.of("Asia/Jakarta")

        // Date
        val date = Date.from(zonedDateTime.toInstant())
        val result1 = DateTime.atZone(date, targetZoneId)
        val simpleDateFormat = SimpleDateFormat(DateTime.DEFAULT_DATE_TIME_PATTERN)
        assertEquals("2024-11-06 23:00:00", simpleDateFormat.format(result1))

        // LocalDate
        val localDate = zonedDateTime.toLocalDate()
        val result2 = DateTime.atZone(localDate, targetZoneId)
        assertEquals("2024-11-06", result2.toString())

        // LocalDateTime
        val localDateTime = zonedDateTime.toLocalDateTime()
        val result3 = DateTime.atZone(localDateTime, targetZoneId).toString()
        assertEquals("2024", result3.substring(0, 4))
        assertEquals("11", result3.substring(5, 7))
        assertEquals("06", result3.substring(8, 10))
        assertEquals("23", result3.substring(11, 13))

        // LocalDate as string
        val result4 = DateTime.atZone("2024-11-07", targetZoneId)
        assertEquals("2024-11-06", result4.toString())

        // LocalDateTime as string
        val result5 = DateTime.atZone("2024-11-07 00:00:00.000", targetZoneId).toString()
        assertEquals("2024", result5.substring(0, 4))
        assertEquals("11", result5.substring(5, 7))
        assertEquals("06", result5.substring(8, 10))
        assertEquals("23", result5.substring(11, 13))

        // Unsupported datetime format
        val e3 = assertThrows<IllegalArgumentException> {
            DateTime.atZone("notParsableDateTimeString", targetZoneId)
        }
        assertEquals("Unsupported date time format: notParsableDateTimeString", e3.message)

        // Unsupported type
        val e4 = assertThrows<IllegalArgumentException> {
            DateTime.atZone(System.currentTimeMillis(), targetZoneId)
        }
        assertEquals("Unsupported type: java.lang.Long", e4.message)
    }

    @Test
    fun testIsSameZone() {
        val e1 = assertThrows<IllegalArgumentException> {
            DateTime.isSameZone(null, ZoneId.systemDefault())
        }
        assertEquals("zoneId or anotherZoneId is null", e1.message)

        val e2 = assertThrows<IllegalArgumentException> {
            DateTime.isSameZone(ZoneId.systemDefault(), null)
        }
        assertEquals("zoneId or anotherZoneId is null", e2.message)

        val defaultZoneId = ZoneId.systemDefault();
        assertTrue { DateTime.isSameZone(defaultZoneId, defaultZoneId) }
        assertTrue { DateTime.isSameZone(ZoneId.systemDefault(), ZoneId.systemDefault()) }
        assertTrue { DateTime.isSameZone(ZoneId.of("Asia/Jakarta"), ZoneId.of("Asia/Jakarta")) }
        assertFalse { DateTime.isSameZone(ZoneId.of("Asia/Jakarta"), ZoneId.of("Asia/Shanghai")) }
    }

    @Test
    fun testMaxLocalDateTime() {
        val localDateTime1 = LocalDateTime.of(2024, 11, 7, 0, 0, 0)
        val localDateTime2 = LocalDateTime.of(2024, 11, 7, 0, 0, 1)
        val localDateTime3 = LocalDateTime.of(2024, 11, 6, 23, 59, 59)
        assertEquals(localDateTime2, DateTime.max(localDateTime1, localDateTime2, localDateTime3))
    }

    @Test
    fun testMinLocalDateTime() {
        val localDateTime1 = LocalDateTime.of(2024, 11, 7, 0, 0, 0)
        val localDateTime2 = LocalDateTime.of(2024, 11, 7, 0, 0, 1)
        val localDateTime3 = LocalDateTime.of(2024, 11, 6, 23, 59, 59)
        assertEquals(localDateTime3, DateTime.min(localDateTime1, localDateTime2, localDateTime3))
    }

    @Test
    fun testMaxLocalDate() {
        val localDate1 = LocalDate.of(2024, 11, 7)
        val localDate2 = LocalDate.of(2024, 11, 6)
        val localDate3 = LocalDate.of(2024, 11, 8)
        assertEquals(localDate3, DateTime.max(localDate1, localDate2, localDate3))
    }

    @Test
    fun testMinLocalDate() {
        val localDate1 = LocalDate.of(2024, 11, 8)
        val localDate2 = LocalDate.of(2024, 11, 7)
        val localDate3 = LocalDate.of(2024, 11, 6)
        assertEquals(localDate3, DateTime.min(localDate1, localDate2, localDate3))
    }

    @Test
    fun testDiffDays() {
        assertEquals(2, DateTime.diffDays("2024-11-06 00:00:00", "2024-11-07 23:59:59"))
        assertEquals(1, DateTime.diffDays("2024-11-06 00:00:00", "2024-11-06 23:59:59"))
        assertEquals(0, DateTime.diffDays("2024-11-06 00:00:00", "2024-11-06 00:00:00"))
        assertEquals(1, DateTime.diffDays("2024-11-06 23:59:59", "2024-11-06 00:00:00"))
        assertEquals(1, DateTime.diffDays("2024-11-07 11:00:00", "2024-11-06 11:00:00"))

        val e1 = assertThrows<IllegalArgumentException>() {
            DateTime.diffDays("notParsableDateTimeString", "2024-11-06 00:00:00")
        }
        assertEquals("Unsupported date time format: notParsableDateTimeString", e1.message)

        val e2 = assertThrows<IllegalArgumentException>() {
            DateTime.diffDays("2024-11-06 00:00:00", "notParsableDateTimeString")
        }
        assertEquals("Unsupported date time format: notParsableDateTimeString", e2.message)

        val e3 = assertThrows<IllegalArgumentException>() {
            DateTime.diffDays("2024-11-06 01:02:03", "2024-11-06 02:03:04")
        }
        assertEquals("The difference between two date time is not a multiple of 24 hours", e3.message)
    }

    @Test
    fun testStartOfToday() {
        val startOfToday = DateTime.startOfToday()
        val now = LocalDateTime.now()
        assertEquals(now.year, startOfToday.substring(0, 4).toInt())
        assertEquals(now.monthValue, startOfToday.substring(5, 7).toInt())
        assertEquals(now.dayOfMonth, startOfToday.substring(8, 10).toInt())
        assertEquals(0, startOfToday.substring(11, 13).toInt())
        assertEquals(0, startOfToday.substring(14, 16).toInt())
        assertEquals(0, startOfToday.substring(17, 19).toInt())
    }

    @Test
    fun testEndOfToday() {
        val endOfToday = DateTime.endOfToday()
        val now = LocalDateTime.now()
        assertEquals(now.year, endOfToday.substring(0, 4).toInt())
        assertEquals(now.monthValue, endOfToday.substring(5, 7).toInt())
        assertEquals(now.dayOfMonth, endOfToday.substring(8, 10).toInt())
        assertEquals(23, endOfToday.substring(11, 13).toInt())
        assertEquals(59, endOfToday.substring(14, 16).toInt())
        assertEquals(59, endOfToday.substring(17, 19).toInt())
    }

}
