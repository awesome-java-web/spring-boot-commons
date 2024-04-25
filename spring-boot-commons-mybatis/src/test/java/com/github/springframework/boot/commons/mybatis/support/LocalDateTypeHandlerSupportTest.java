package com.github.springframework.boot.commons.mybatis.support;

import org.junit.jupiter.api.Test;

import java.sql.CallableStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class LocalDateTypeHandlerSupportTest {

	@Test
	void testGetNullableResult() throws SQLException {
		LocalDateTypeHandlerSupport localDateTypeHandler = new LocalDateTypeHandlerSupport();

		ResultSet rs = mock(ResultSet.class);
		final String columnName = "columnName";
		final int columnIndex = 1;
		CallableStatement cs = mock(CallableStatement.class);
		when(rs.getObject(columnName)).thenReturn(Timestamp.valueOf(LocalDateTime.now()));
		when(rs.getObject(columnIndex)).thenReturn(Timestamp.valueOf(LocalDateTime.now()));
		when(cs.getObject(columnIndex)).thenReturn(Timestamp.valueOf(LocalDateTime.now()));

		LocalDate result = localDateTypeHandler.getNullableResult(rs, columnName);
		assertEquals(LocalDate.now(), result);

		result = localDateTypeHandler.getNullableResult(rs, columnIndex);
		assertEquals(LocalDate.now(), result);

		result = localDateTypeHandler.getNullableResult(cs, columnIndex);
		assertEquals(LocalDate.now(), result);

		rs.close();
		cs.close();
	}

	@Test
	void testUnexpectedReturnType() throws SQLException {
		LocalDateTypeHandlerSupport localDateTypeHandler = new LocalDateTypeHandlerSupport();

		ResultSet rs = mock(ResultSet.class);
		final String columnName = "columnName";
		when(rs.getObject(columnName)).thenReturn("This is a string instead of an instance of Timestamp");

		LocalDate result = localDateTypeHandler.getNullableResult(rs, columnName);
		assertNull(result);

		rs.close();
	}

}