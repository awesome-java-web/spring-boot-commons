package com.github.springframework.boot.commons.util;

public enum CharConstants {

    AT('@'),

    DOT('.'),

    DASH('-'),

    COMMA(','),

    UNDERLINE('_'),

    ;

    private final char charValue;

    private final String stringValue;

    CharConstants(char charValue) {
        this.charValue = charValue;
        this.stringValue = String.valueOf(charValue);
    }

    public char charValue() {
        return charValue;
    }

    public String stringValue() {
        return stringValue;
    }

}
