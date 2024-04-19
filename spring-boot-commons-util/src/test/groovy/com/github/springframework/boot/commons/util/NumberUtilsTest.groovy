package com.github.springframework.boot.commons.util

import spock.lang.Specification
import spock.lang.Unroll

class NumberUtilsTest extends Specification {

	def "test new constructor"() {
		when:
		new NumberUtils()

		then:
		Exception e = thrown(UnsupportedOperationException)
		e instanceof UnsupportedOperationException && e.message == "Utility class should not be instantiated"
	}

	@Unroll
	def "test defaultIfNull(#number, #defaultValue)"() {
		expect:
		NumberUtils.defaultIfNull(number, defaultValue) == expectedResult

		where:
		number         | defaultValue    | expectedResult
		null           | 1024            | 1024
		1024           | 2048            | 1024
		null           | 1024L           | 1024L
		1024L          | 2048L           | 1024L
		null           | 1024.0F         | 1024.0F
		1024.0F        | 2048.0F         | 1024.0F
		null           | 1024.0D         | 1024.0D
		1024.0D        | 2048.0D         | 1024.0D
		null           | BigInteger.ZERO | BigInteger.ZERO
		BigInteger.ONE | BigInteger.ZERO | BigInteger.ONE
		null           | BigDecimal.ZERO | BigDecimal.ZERO
		BigDecimal.ONE | BigDecimal.ZERO | BigDecimal.ONE
	}

	@Unroll
	def "test zeroIfNull(#number)"() {
		expect:
		NumberUtils.zeroIfNull(number) == expectedResult

		where:
		number         | expectedResult
		1024           | 1024
		1024L          | 1024L
		1024.0F        | 1024.0F
		1024.0D        | 1024.0D
		BigInteger.ONE | BigInteger.ONE
		BigDecimal.ONE | BigDecimal.ONE
	}

}
