package com.github.springframework.boot.commons.util

import org.mockito.MockedStatic
import org.mockito.Mockito
import spock.lang.Specification
import spock.lang.Unroll

import java.security.MessageDigest
import java.security.NoSuchAlgorithmException

class MessageDigestUtilsTest extends Specification {

	def "test new constructor"() {
		when:
		new MessageDigestUtils()

		then:
		Exception e = thrown(UnsupportedOperationException)
		e instanceof UnsupportedOperationException && e.message == "Utility class should not be instantiated"
	}

	def "test md5 catch NoSuchAlgorithmException"() {
		given:
		MockedStatic<MessageDigest> md = Mockito.mockStatic(MessageDigest.class)
		md.when(() -> MessageDigest.getInstance(Mockito.anyString())).thenThrow(NoSuchAlgorithmException)

		expect:
		MessageDigestUtils.md5("hello world").length == 0

		cleanup:
		md.close()
	}

	@Unroll
	def "test md5Hex, input = #input, result = #result"() {
		expect:
		MessageDigestUtils.md5Hex(input) == result

		where:
		input         | result
		"hello world" | "5eb63bbbe01eeed093cb22bb8f5acdc3"
		"12345678910" | "432f45b44c432414d2f97df0e5743818"
		"test md5Hex" | "23d03bcab51446d6b9a50aaf26ebe666"
	}

}
