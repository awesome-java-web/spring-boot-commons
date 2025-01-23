package com.github.awesome.springboot.commons.base

import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import java.math.BigDecimal
import java.math.RoundingMode
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertTrue

class NumbersTest {

    @Test
    fun testNewInstance() {
        val e = assertThrows<UnsupportedOperationException> {
            Numbers()
        }
        assertEquals("Utility class should not be instantiated", e.message)
    }

    @Test
    fun testDefaultIfNull() {
        assertEquals(1, Numbers.defaultIfNull(null, 1))
        assertEquals(BigDecimal.ZERO, Numbers.defaultIfNull(null, BigDecimal.ZERO))
        assertEquals(1, Numbers.defaultIfNull(1, 0))
        assertEquals(BigDecimal.ONE, Numbers.defaultIfNull(BigDecimal.ONE, BigDecimal.ZERO))
    }

    @Test
    fun testIsZero() {
        assertTrue { Numbers.isZero(BigDecimal("0.00")) }
        assertTrue { Numbers.isZero(BigDecimal("0.0")) }
        assertTrue { Numbers.isZero(BigDecimal("0")) }
        assertTrue { Numbers.isZero(BigDecimal.ZERO) }
        assertFalse { Numbers.isZero(BigDecimal.ONE) }
        assertFalse { Numbers.isZero(null) }
    }

    @Test
    fun testIsPositive() {
        assertTrue { Numbers.isPositive(BigDecimal.ONE) }
        assertFalse { Numbers.isPositive(null) }
        assertFalse { Numbers.isPositive(BigDecimal.valueOf(-1)) }
    }

    @Test
    fun testIsNegative() {
        assertTrue { Numbers.isNegative(BigDecimal.valueOf(-1)) }
        assertFalse { Numbers.isNegative(null) }
        assertFalse { Numbers.isNegative(BigDecimal.ONE) }
    }

    @Test
    fun testToPlainString() {
        assertEquals("1", Numbers.toPlainString(BigDecimal.ONE, 0, RoundingMode.HALF_UP))
        assertEquals("1.0", Numbers.toPlainString(BigDecimal.ONE, 1, RoundingMode.HALF_UP))
        assertEquals("1.00", Numbers.toPlainString(BigDecimal.ONE, 2, RoundingMode.HALF_UP))
        assertEquals("1.23", Numbers.toPlainString(BigDecimal.valueOf(1.23), 2, RoundingMode.HALF_UP))
        assertEquals("1.23", Numbers.toPlainString(BigDecimal.valueOf(1.234), 2, RoundingMode.HALF_UP))
        assertEquals("1.24", Numbers.toPlainString(BigDecimal.valueOf(1.235), 2, RoundingMode.HALF_UP))
    }

    @Test
    fun testToScaledPlainString() {
        assertEquals("1", Numbers.toScaledPlainString(BigDecimal.ONE, 0))
        assertEquals("1.0", Numbers.toScaledPlainString(BigDecimal.ONE, 1))
        assertEquals("1.00", Numbers.toScaledPlainString(BigDecimal.ONE, 2))
        assertEquals("1.23", Numbers.toScaledPlainString(BigDecimal.valueOf(1.23), 2))
        assertEquals("1.23", Numbers.toScaledPlainString(BigDecimal.valueOf(1.234), 2))
        assertEquals("1.24", Numbers.toScaledPlainString(BigDecimal.valueOf(1.235), 2))
    }

    @Test
    fun testIsBigDecimalConvertible() {
        assertTrue { Numbers.isBigDecimalConvertible("1") }
        assertTrue { Numbers.isBigDecimalConvertible("1.0") }
        assertTrue { Numbers.isBigDecimalConvertible("1.00") }
        assertTrue { Numbers.isBigDecimalConvertible("1.23") }
        assertTrue { Numbers.isBigDecimalConvertible("1.234") }
        assertTrue { Numbers.isBigDecimalConvertible("1.235") }
        assertFalse { Numbers.isBigDecimalConvertible("1.23.45") }
        assertFalse { Numbers.isBigDecimalConvertible(Strings.EMPTY) }
        assertFalse { Numbers.isBigDecimalConvertible(null) }
    }

}
