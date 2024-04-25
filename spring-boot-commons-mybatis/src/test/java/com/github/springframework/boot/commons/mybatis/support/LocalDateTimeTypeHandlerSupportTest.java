package com.github.springframework.boot.commons.mybatis.support;

import org.junit.jupiter.api.Test;

import java.sql.CallableStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class LocalDateTimeTypeHandlerSupportTest {

	@Test
	void testGetNullableResult() throws SQLException {
		LocalDateTimeTypeHandlerSupport localDateTimeTypeHandler = new LocalDateTimeTypeHandlerSupport();
		LocalDateTime now = LocalDateTime.now();

		ResultSet rs = mock(ResultSet.class);
		final String columnName = "columnName";
		final int columnIndex = 1;
		CallableStatement cs = mock(CallableStatement.class);
		when(rs.getObject(columnName)).thenReturn(Timestamp.valueOf(now));
		when(rs.getObject(columnIndex)).thenReturn(Timestamp.valueOf(now));
		when(cs.getObject(columnIndex)).thenReturn(Timestamp.valueOf(now));

		LocalDateTime result = localDateTimeTypeHandler.getNullableResult(rs, columnName);
		assertEquals(now, result);

		result = localDateTimeTypeHandler.getNullableResult(rs, columnIndex);
		assertEquals(now, result);

		result = localDateTimeTypeHandler.getNullableResult(cs, columnIndex);
		assertEquals(now, result);

		rs.close();
		cs.close();
	}

	@Test
	void testUnexpectedReturnType() throws SQLException {
		LocalDateTimeTypeHandlerSupport localDateTimeTypeHandler = new LocalDateTimeTypeHandlerSupport();

		ResultSet rs = mock(ResultSet.class);
		final String columnName = "columnName";
		when(rs.getObject(columnName)).thenReturn("This is a string instead of an instance of Timestamp");

		LocalDateTime result = localDateTimeTypeHandler.getNullableResult(rs, columnName);
		assertNull(result);

		rs.close();
	}

}