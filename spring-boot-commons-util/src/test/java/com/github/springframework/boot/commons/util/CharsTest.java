package com.github.springframework.boot.commons.util;

import com.github.springframework.boot.commons.util.base.Chars;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class CharsTest {

    @Test
    void testCharConstants() {
        assertEquals('@', Chars.AT.charValue());
        assertEquals('.', Chars.DOT.charValue());
        assertEquals('-', Chars.DASH.charValue());
        assertEquals(',', Chars.COMMA.charValue());
        assertEquals('_', Chars.UNDERLINE.charValue());
        assertEquals('%', Chars.PERCENT.charValue());

        assertEquals("@", Chars.AT.stringValue());
        assertEquals(".", Chars.DOT.stringValue());
        assertEquals("-", Chars.DASH.stringValue());
        assertEquals(",", Chars.COMMA.stringValue());
        assertEquals("_", Chars.UNDERLINE.stringValue());
        assertEquals("%", Chars.PERCENT.stringValue());
    }

}
