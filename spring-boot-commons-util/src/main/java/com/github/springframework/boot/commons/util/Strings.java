package com.github.springframework.boot.commons.util;

public final class Strings {

    public static final String EMPTY = "";

    private Strings() {
        throw new UnsupportedOperationException("Utility class should not be instantiated");
    }

    public static String defaultIfNull(Object value, String defaultValue) {
        return value == null ? defaultValue : value.toString();
    }

}
