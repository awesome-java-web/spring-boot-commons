package com.github.springframework.boot.commons.util;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class CharConstantsTest {

	@Test
	void testCharConstants() {
		assertEquals('@', CharConstants.AT.charValue());
		assertEquals('.', CharConstants.DOT.charValue());
		assertEquals('-', CharConstants.DASH.charValue());
		assertEquals(',', CharConstants.COMMA.charValue());
		assertEquals("@", CharConstants.AT.stringValue());
		assertEquals(".", CharConstants.DOT.stringValue());
		assertEquals("-", CharConstants.DASH.stringValue());
		assertEquals(",", CharConstants.COMMA.stringValue());
	}

}