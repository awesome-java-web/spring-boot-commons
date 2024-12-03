package com.github.springframework.boot.commons.util;

import com.github.springframework.boot.commons.util.base.Numbers;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import java.math.BigDecimal;
import java.math.BigInteger;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class NumbersTest {

    @Test
    void testNewInstance() {
        UnsupportedOperationException e = assertThrows(UnsupportedOperationException.class, Numbers::new);
        assertEquals("Utility class should not be instantiated", e.getMessage());
    }

    @ParameterizedTest
    @CsvSource({", 1024, 1024", "1024, 2048, 1024"})
    void testDefaultIfNullInteger(Integer number, int defaultValue, int expected) {
        assertEquals(expected, Numbers.defaultIfNull(number, defaultValue));
    }

    @ParameterizedTest
    @CsvSource({", 1024, 1024", "1024, 2048, 1024"})
    void testDefaultIfNullLong(Long number, long defaultValue, long expected) {
        assertEquals(expected, Numbers.defaultIfNull(number, defaultValue));
    }

    @ParameterizedTest
    @CsvSource({", 1024.0, 1024.0", "1024.0, 2048.0, 1024.0"})
    void testDefaultIfNullFloat(Float number, float defaultValue, float expected) {
        assertEquals(expected, Numbers.defaultIfNull(number, defaultValue));
    }

    @ParameterizedTest
    @CsvSource({", 1024.0, 1024.0", "1024.0, 2048.0, 1024.0"})
    void testDefaultIfNullDouble(Double number, double defaultValue, double expected) {
        assertEquals(expected, Numbers.defaultIfNull(number, defaultValue));
    }

    @ParameterizedTest
    @CsvSource({", 1024, 1024", "1024, 2048, 1024"})
    void testDefaultIfNullBigInteger(BigInteger number, BigInteger defaultValue, BigInteger expected) {
        assertEquals(expected, Numbers.defaultIfNull(number, defaultValue));
    }

    @ParameterizedTest
    @CsvSource({", 1024.0, 1024.0", "1024.0, 2048.0, 1024.0"})
    void testDefaultIfNullBigDecimal(BigDecimal number, BigDecimal defaultValue, BigDecimal expected) {
        assertEquals(expected, Numbers.defaultIfNull(number, defaultValue));
    }

    @ParameterizedTest
    @CsvSource({", 0", "1024, 1024"})
    void testZeroIfNullInteger(Integer number, int expected) {
        assertEquals(expected, Numbers.zeroIfNull(number));
    }

    @ParameterizedTest
    @CsvSource({", 0", "1024, 1024"})
    void testZeroIfNullLong(Long number, long expected) {
        assertEquals(expected, Numbers.zeroIfNull(number));
    }

    @ParameterizedTest
    @CsvSource({", 0.0", "1024.0, 1024.0"})
    void testZeroIfNullFloat(Float number, float expected) {
        assertEquals(expected, Numbers.zeroIfNull(number));
    }

    @ParameterizedTest
    @CsvSource({", 0.0", "1024.0, 1024.0"})
    void testZeroIfNullDouble(Double number, double expected) {
        assertEquals(expected, Numbers.zeroIfNull(number));
    }

    @ParameterizedTest
    @CsvSource({", 0", "1024, 1024"})
    void testZeroIfNullBigInteger(BigInteger number, BigInteger expected) {
        assertEquals(expected, Numbers.zeroIfNull(number));
    }

    @ParameterizedTest
    @CsvSource({", 0", "1024, 1024"})
    void testZeroIfNullBigDecimal(BigDecimal number, BigDecimal expected) {
        assertEquals(expected, Numbers.zeroIfNull(number));
    }

    @ParameterizedTest
    @CsvSource({
        ", false", "'', false", "+, false", "-, false", "123, true", "123.45, true",
        "+123.45, true", "-123.45, true", "123+45, false", "123-45, false", "123.45.67, false"
    })
    void testCanBeConvertedToBigDecimal(String number, boolean expected) {
        assertEquals(expected, Numbers.canBeConvertedToBigDecimal(number));
    }

}
