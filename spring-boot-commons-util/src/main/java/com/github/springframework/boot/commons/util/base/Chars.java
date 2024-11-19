package com.github.springframework.boot.commons.util.base;

public enum Chars {

    AT('@'),

	AND('&'),

    DOT('.'),

    DASH('-'),

    COMMA(','),

	EQUAL('='),

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
