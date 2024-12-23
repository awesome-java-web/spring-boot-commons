package com.github.awesome.springboot.commons.base

import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import java.math.BigDecimal
import kotlin.test.assertEquals

class MapsTest {

    @Test
    fun testNewInstance() {
        val e = assertThrows<UnsupportedOperationException>() {
            Maps()
        }
        assertEquals("Utility class should not be instantiated", e.message)
    }

    @Test
    fun testGetOrEmptyString() {
        val nullMap: Map<String, String>? = null
        assertEquals(Strings.EMPTY, Maps.getOrEmptyString(nullMap, "key"))

        val map: Map<String, String> = mapOf("key1" to "value1", "key2" to "value2")
        assertEquals(Strings.EMPTY, Maps.getOrEmptyString(map, null))
        assertEquals(Strings.EMPTY, Maps.getOrEmptyString(map, "key3"))
        assertEquals("value1", Maps.getOrEmptyString(map, "key1"))
    }

    @Test
    fun testGetBigDecimalOrZero() {
        val nullMap: Map<String, BigDecimal>? = null
        assertEquals(BigDecimal.ZERO, Maps.getBigDecimalOrZero(nullMap, "key"))

        val map: Map<String, BigDecimal> = mapOf("key1" to BigDecimal.ONE, "key2" to BigDecimal.TEN)
        assertEquals(BigDecimal.ZERO, Maps.getBigDecimalOrZero(map, null))
        assertEquals(BigDecimal.ZERO, Maps.getBigDecimalOrZero(map, "key3"))
        assertEquals(BigDecimal.ONE, Maps.getBigDecimalOrZero(map, "key1"))
    }

}
