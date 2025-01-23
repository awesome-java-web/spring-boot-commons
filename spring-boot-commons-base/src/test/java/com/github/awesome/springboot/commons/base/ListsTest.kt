package com.github.awesome.springboot.commons.base

import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import java.util.*
import kotlin.test.assertEquals

class ListsTest {

    @Test
    fun testNewInstance() {
        val e = assertThrows<UnsupportedOperationException> {
            Lists()
        }
        assertEquals("Utility class should not be instantiated", e.message)
    }

    @Test
    fun testSafeGet() {
        val nullList: List<Int>? = null
        assertEquals(Optional.empty(), Lists.safeGet(nullList, 0))

        val emptyList: List<Int> = listOf()
        assertEquals(Optional.empty(), Lists.safeGet(emptyList, 0))

        val list: List<Int> = listOf(1, 2, 3)
        assertEquals(0, Lists.safeGet(list, -1).orElse(0))
        assertEquals(1, Lists.safeGet(list, 0).orElse(0))
        assertEquals(2, Lists.safeGet(list, 1).orElse(0))
        assertEquals(3, Lists.safeGet(list, 2).orElse(0))
        assertEquals(0, Lists.safeGet(list, 3).orElse(0))
    }

    @Test
    fun testFirstOf() {
        val nullList: List<Int>? = null
        assertEquals(Optional.empty(), Lists.safeGet(nullList, 0))
        assertEquals(Optional.empty(), Lists.safeGet(nullList, 1))
        assertEquals(Optional.empty(), Lists.safeGet(nullList, -1))

        val list: List<Int> = listOf(1, 2, 3)
        assertEquals(1, Lists.firstOf(list).orElse(0))
    }

}
