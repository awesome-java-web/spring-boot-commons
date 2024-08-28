package com.github.springframework.boot.commons.util;

import java.math.BigDecimal;
import java.util.Map;

public final class SafeMapReader {

    private SafeMapReader() {
        throw new UnsupportedOperationException("Utility class should not be instantiated");
    }

    public static <K> String emptyStringIfAbsent(Map<K, Object> map, K key) {
        return (map == null || key == null) ? Strings.EMPTY : map.getOrDefault(key, Strings.EMPTY).toString();
    }

    public static <K> BigDecimal zeroBigDecimalIfAbsent(Map<K, BigDecimal> map, K key) {
        return (map == null || key == null) ? BigDecimal.ZERO : map.getOrDefault(key, BigDecimal.ZERO);
    }

}
