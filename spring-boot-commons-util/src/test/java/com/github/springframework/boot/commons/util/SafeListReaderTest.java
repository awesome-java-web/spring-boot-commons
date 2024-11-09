package com.github.springframework.boot.commons.util;

import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class SafeListReaderTest {

	@Test
	void testNewInstance() {
		UnsupportedOperationException e = assertThrows(UnsupportedOperationException.class, SafeListReader::new);
		assertEquals("Utility class should not be instantiated", e.getMessage());
	}

	@Test
	void testGetByIndex() {
		List<Integer> list = Arrays.asList(1, 2, 3, 4, 5);
		assertNull(SafeListReader.getByIndex(null, 0));
		assertNull(SafeListReader.getByIndex(list, -1));
		assertNull(SafeListReader.getByIndex(list, list.size()));
		assertEquals(1, SafeListReader.getByIndex(list, 0));
	}

}
