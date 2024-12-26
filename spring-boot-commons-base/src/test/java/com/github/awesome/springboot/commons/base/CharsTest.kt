package com.github.awesome.springboot.commons.base

import org.junit.jupiter.api.Test
import kotlin.test.assertEquals

class CharsTest {

    @Test
    fun testChars() {
        assertEquals('@', Chars.AT.charValue())
        assertEquals('&', Chars.AND.charValue())
        assertEquals('.', Chars.DOT.charValue())
        assertEquals('-', Chars.DASH.charValue())
        assertEquals(',', Chars.COMMA.charValue())
        assertEquals('=', Chars.EQUAL.charValue())
        assertEquals('_', Chars.UNDERLINE.charValue())
        assertEquals('%', Chars.PERCENT.charValue())
        assertEquals('*', Chars.WILDCARD.charValue())
        assertEquals(' ', Chars.WHITESPACE.charValue())
        assertEquals('`', Chars.BACKTICK.charValue())

        assertEquals("@", Chars.AT.stringValue())
        assertEquals("&", Chars.AND.stringValue())
        assertEquals(".", Chars.DOT.stringValue())
        assertEquals("-", Chars.DASH.stringValue())
        assertEquals(",", Chars.COMMA.stringValue())
        assertEquals("=", Chars.EQUAL.stringValue())
        assertEquals("_", Chars.UNDERLINE.stringValue())
        assertEquals("%", Chars.PERCENT.stringValue())
        assertEquals("*", Chars.WILDCARD.stringValue())
        assertEquals(" ", Chars.WHITESPACE.stringValue())
        assertEquals("`", Chars.BACKTICK.stringValue())
    }

}
