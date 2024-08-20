package com.github.springframework.boot.commons.util;

import java.util.Map;

public final class SafeMapReader {

    private SafeMapReader() {
        throw new UnsupportedOperationException("Utility class should not be instantiated");
    }

    public static <K> Object nullSafeGetOrDefault(Map<K, Object> map, K key, Object defaultValue) {
        return (map == null || key == null) ? defaultValue : map.getOrDefault(key, defaultValue);
    }

    public static <K> String emptyStringIfAbsent(Map<K, Object> map, K key) {
        return nullSafeGetOrDefault(map, key, Strings.EMPTY).toString();
    }

}
