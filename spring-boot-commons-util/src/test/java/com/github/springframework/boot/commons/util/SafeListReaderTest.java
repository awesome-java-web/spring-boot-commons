package com.github.springframework.boot.commons.util;

import com.github.springframework.boot.commons.util.safe.SafeListReader;
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
	void testSafeGetByIndex() {
		List<Integer> list = Arrays.asList(1, 2, 3, 4, 5);
		assertNull(SafeListReader.safeGetByIndex(null, 0));
		assertNull(SafeListReader.safeGetByIndex(list, -1));
		assertNull(SafeListReader.safeGetByIndex(list, list.size()));
		assertEquals(1, SafeListReader.safeGetByIndex(list, 0));
	}

}
