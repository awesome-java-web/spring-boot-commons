package com.github.springframework.boot.commons.util.base;

import java.math.BigDecimal;
import java.math.BigInteger;

public final class Numbers {

    public Numbers() {
        throw new UnsupportedOperationException("Utility class should not be instantiated");
    }

    public static int defaultIfNull(Integer number, int defaultValue) {
        return number == null ? defaultValue : number;
    }

    public static long defaultIfNull(Long number, long defaultValue) {
        return number == null ? defaultValue : number;
    }

    public static float defaultIfNull(Float number, float defaultValue) {
        return number == null ? defaultValue : number;
    }

    public static double defaultIfNull(Double number, double defaultValue) {
        return number == null ? defaultValue : number;
    }

    public static BigInteger defaultIfNull(BigInteger number, BigInteger defaultValue) {
        return number == null ? defaultValue : number;
    }

    public static BigDecimal defaultIfNull(BigDecimal number, BigDecimal defaultValue) {
        return number == null ? defaultValue : number;
    }

    public static int zeroIfNull(Integer number) {
        return defaultIfNull(number, 0);
    }

    public static long zeroIfNull(Long number) {
        return defaultIfNull(number, 0L);
    }

    public static float zeroIfNull(Float number) {
        return defaultIfNull(number, 0.0F);
    }

    public static double zeroIfNull(Double number) {
        return defaultIfNull(number, 0.0D);
    }

    public static BigInteger zeroIfNull(BigInteger number) {
        return defaultIfNull(number, BigInteger.ZERO);
    }

    public static BigDecimal zeroIfNull(BigDecimal number) {
        return defaultIfNull(number, BigDecimal.ZERO);
    }

    public static boolean isCurrencyAmount(String number) {
        if (number == null || number.isEmpty()) {
            return false;
        }

        try {
            new BigDecimal(number);
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }

}
