package com.github.awesome.springboot.commons.base

import com.alibaba.fastjson.JSONArray
import com.alibaba.fastjson.JSONObject
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import kotlin.test.assertEquals

class FastJsonTest {

    @Test
    fun testNewInstance() {
        val e = assertThrows<UnsupportedOperationException> {
            FastJson()
        }
        assertEquals("Utility class should not be instantiated", e.message)
    }

    @Test
    fun testGetOrEmptyJsonObject() {
        assertEquals(JSONObject(), FastJson.getOrEmptyJsonObject(null, "key"))
        assertEquals(JSONObject(), FastJson.getOrEmptyJsonObject(null, 0))
        assertEquals(JSONObject(), FastJson.getOrEmptyJsonObject(JSONArray(), 0))

        val jsonObject = JSONObject()
        val innerJsonObject = JSONObject()
        innerJsonObject["innerKey"] = "innerValue"
        jsonObject["inner"] = innerJsonObject
        assertEquals(innerJsonObject, FastJson.getOrEmptyJsonObject(jsonObject, "inner"))
        assertEquals(JSONObject(), FastJson.getOrEmptyJsonObject(jsonObject, "notExist"))

        val jsonArray = JSONArray()
        jsonArray.add(null)
        jsonArray.add(innerJsonObject)
        assertEquals(JSONObject(), FastJson.getOrEmptyJsonObject(jsonArray, -1))
        assertEquals(JSONObject(), FastJson.getOrEmptyJsonObject(jsonArray, 2))
        assertEquals(JSONObject(), FastJson.getOrEmptyJsonObject(jsonArray, 0))
        assertEquals(innerJsonObject, FastJson.getOrEmptyJsonObject(jsonArray, 1))
    }

    @Test
    fun testGetOrEmptyJsonArray() {
        assertEquals(JSONArray(), FastJson.getOrEmptyJsonArray(null, "key"))

        val jsonObject = JSONObject()
        val innerJsonArray = JSONArray()
        innerJsonArray.add("innerValue")
        jsonObject["inner"] = innerJsonArray
        assertEquals(innerJsonArray, FastJson.getOrEmptyJsonArray(jsonObject, "inner"))
        assertEquals(JSONArray(), FastJson.getOrEmptyJsonArray(jsonObject, "notExist"))
    }

    @Test
    fun testParseOrReturnEmptyJsonObject() {
        assertEquals(JSONObject(), FastJson.parseOrReturnEmptyJsonObject(null))
        assertEquals(JSONObject(), FastJson.parseOrReturnEmptyJsonObject(Strings.EMPTY))
        assertEquals(JSONObject(), FastJson.parseOrReturnEmptyJsonObject(Chars.WHITESPACE.stringValue()))
        assertEquals(JSONObject(), FastJson.parseOrReturnEmptyJsonObject("{}"))
    }

    @Test
    fun testParseOrReturnEmptyJsonArray() {
        assertEquals(JSONArray(), FastJson.parseOrReturnEmptyJsonArray(null))
        assertEquals(JSONArray(), FastJson.parseOrReturnEmptyJsonArray(Strings.EMPTY))
        assertEquals(JSONArray(), FastJson.parseOrReturnEmptyJsonArray(Chars.WHITESPACE.stringValue()))
        assertEquals(JSONArray(), FastJson.parseOrReturnEmptyJsonArray("[]"))
    }

}
