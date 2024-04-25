package com.github.springframework.boot.commons.util;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.mockito.MockedStatic;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.mockStatic;

class MessageDigestUtilsTest {

	@Test
	void testNewInstance() {
		UnsupportedOperationException e = assertThrows(UnsupportedOperationException.class, MessageDigestUtils::new);
		assertEquals("Utility class should not be instantiated", e.getMessage());
	}

	@ParameterizedTest
	@CsvSource({
			"hello world, 5eb63bbbe01eeed093cb22bb8f5acdc3",
			"12345678910, 432f45b44c432414d2f97df0e5743818",
			"test md5Hex, 23d03bcab51446d6b9a50aaf26ebe666"
	})
	void testMd5Hex(String input, String expected) {
		assertEquals(expected, MessageDigestUtils.md5Hex(input));
	}

	@Test
	void testNoSuchAlgorithmException() {
		MockedStatic<MessageDigest> mockMessageDigest = mockStatic(MessageDigest.class);
		mockMessageDigest.when(() -> MessageDigest.getInstance(anyString())).thenThrow(new NoSuchAlgorithmException());
		assertEquals(0, MessageDigestUtils.md5(anyString()).length);
		mockMessageDigest.close();
	}

}