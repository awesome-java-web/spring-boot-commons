package com.github.springframework.boot.commons.util;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class StringsTest {

	@Test
	void testNewInstance() {
		UnsupportedOperationException e = assertThrows(UnsupportedOperationException.class, Strings::new);
		assertEquals("Utility class should not be instantiated", e.getMessage());
	}

	@Test
	void testDefaultIfNull() {
		assertEquals(Strings.EMPTY, Strings.defaultIfNull(null, Strings.EMPTY));
		assertEquals("testValue", Strings.defaultIfNull("testValue", "testDefaultValue"));
	}

	@Test
	void testIsEmailAddress() {
		assertFalse(Strings.isEmailAddress(null));
		assertFalse(Strings.isEmailAddress("test"));
		assertTrue(Strings.isEmailAddress("test@test.com"));
	}

}
