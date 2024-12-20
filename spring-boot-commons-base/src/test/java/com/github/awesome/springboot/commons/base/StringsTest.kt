package com.github.awesome.springboot.commons.base;

import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import kotlin.test.assertEquals

class StringsTest {

    @Test
    fun testNewInstance() {
        val e = assertThrows<UnsupportedOperationException> {
            Strings()
        }
        assertEquals("Utility class should not be instantiated", e.message)
    }

    @Test
    fun testDefaultIfNullOrEmpty() {
        assertEquals("default", Strings.defaultIfNullOrEmpty(null, "default"))
        assertEquals("default", Strings.defaultIfNullOrEmpty(Strings.EMPTY, "default"))
        assertEquals("value", Strings.defaultIfNullOrEmpty("value", "default"))
    }

}
