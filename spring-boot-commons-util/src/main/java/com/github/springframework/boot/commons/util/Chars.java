package com.github.springframework.boot.commons.util;

public enum Chars {

    AT('@'),

    DOT('.'),

    DASH('-'),

    COMMA(','),

    UNDERLINE('_'),

    PERCENT('%'),

    ;

    private final char charValue;

    private final String stringValue;

    Chars(char charValue) {
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
