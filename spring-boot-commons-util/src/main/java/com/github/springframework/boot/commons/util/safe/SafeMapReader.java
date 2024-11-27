package com.github.springframework.boot.commons.util.safe;

import org.apache.logging.log4j.util.Strings;

import java.math.BigDecimal;
import java.util.Map;

public final class SafeMapReader {

    public SafeMapReader() {
        throw new UnsupportedOperationException("Utility class should not be instantiated");
    }

    public static <K, V> String getOrEmptyString(Map<K, V> map, K key) {
        if (map == null || key == null) {
            return Strings.EMPTY;
        }
        V value = map.get(key);
        return value == null ? Strings.EMPTY : value.toString();
    }

    public static <K> BigDecimal getOrBigDecimalZero(Map<K, BigDecimal> map, K key) {
        return (map == null || key == null) ? BigDecimal.ZERO : map.getOrDefault(key, BigDecimal.ZERO);
    }

}
