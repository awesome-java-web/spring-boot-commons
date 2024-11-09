package com.github.springframework.boot.commons.util;

import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class SafeMapReaderTest {

	@Test
	void testNewInstance() {
		UnsupportedOperationException e = assertThrows(UnsupportedOperationException.class, SafeMapReader::new);
		assertEquals("Utility class should not be instantiated", e.getMessage());
	}

	@Test
	void testGetOrEmptyString() {
		Map<String, String> map = new HashMap<>();
		map.put("test1", "value1");
		map.put("test2", null);
		assertEquals(Strings.EMPTY, SafeMapReader.getOrEmptyString(null, "test1"));
		assertEquals(Strings.EMPTY, SafeMapReader.getOrEmptyString(map, null));
		assertEquals(Strings.EMPTY, SafeMapReader.getOrEmptyString(map, "test3"));
		assertEquals("value1", SafeMapReader.getOrEmptyString(map, "test1"));
	}

	@Test
	void testGetOrBigDecimalZero() {
		Map<String, BigDecimal> map = new HashMap<>();
		map.put("test1", BigDecimal.ONE);
		map.put("test2", null);
		assertEquals(BigDecimal.ZERO, SafeMapReader.getOrBigDecimalZero(null, "test1"));
		assertEquals(BigDecimal.ZERO, SafeMapReader.getOrBigDecimalZero(map, null));
		assertEquals(BigDecimal.ZERO, SafeMapReader.getOrBigDecimalZero(map, "test3"));
		assertEquals(BigDecimal.ONE, SafeMapReader.getOrBigDecimalZero(map, "test1"));
	}

}
