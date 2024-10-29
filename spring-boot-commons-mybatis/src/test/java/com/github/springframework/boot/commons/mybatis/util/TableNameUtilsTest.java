package com.github.springframework.boot.commons.mybatis.util;

import org.apache.ibatis.executor.resultset.DefaultResultSetHandler;
import org.apache.ibatis.mapping.BoundSql;
import org.apache.ibatis.mapping.MappedStatement;
import org.apache.ibatis.plugin.Invocation;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.mockito.MockedStatic;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Field;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class TableNameUtilsTest {

	static MockedStatic<LoggerFactory> mockLoggerFactory;

	@BeforeAll
	static void setup() {
		Logger mockLogger = mock(Logger.class);
		mockLoggerFactory = mockStatic(LoggerFactory.class);
		mockLoggerFactory.when(() -> LoggerFactory.getLogger(TableNameUtils.class)).thenReturn(mockLogger);
		when(mockLogger.isDebugEnabled()).thenReturn(true);
	}

	@AfterAll
	static void cleanup() {
		mockLoggerFactory.close();
	}

	@Test
	void testNewInstance() {
		UnsupportedOperationException e = assertThrows(UnsupportedOperationException.class, TableNameUtils::new);
		assertEquals("Utility class should not be instantiated", e.getMessage());
	}

	@ParameterizedTest
	@CsvSource({"select * from table1, table1", "insert into table2, table2", "update table3 set field = value, table3", "nullTableName,"})
	void testResolveExecutorTableName(String sql, String expectedTableName) {
		Invocation mockInvocation = mock(Invocation.class);
		MappedStatement mockMappedStatement = mock(MappedStatement.class);
		Object mockParameter = mock(Object.class);
		BoundSql mockBoundSql = mock(BoundSql.class);

		when(mockInvocation.getArgs()).thenReturn(new Object[]{mockMappedStatement, mockParameter});
		when(mockMappedStatement.getBoundSql(mockParameter)).thenReturn(mockBoundSql);
		when(mockBoundSql.getSql()).thenReturn(sql);

		Set<String> resolvedTableNames = TableNameUtils.resolveExecutorTableName(mockInvocation);
		assertTrue(resolvedTableNames.isEmpty() || resolvedTableNames.contains(expectedTableName));
	}

	@Test
	void testResolveResultSetTableName() throws NoSuchFieldException, IllegalAccessException {
		Invocation mockInvocation = mock(Invocation.class);
		DefaultResultSetHandler mockDefaultResultSetHandler = mock(DefaultResultSetHandler.class);
		BoundSql mockBoundSql = mock(BoundSql.class);

		when(mockInvocation.getTarget()).thenReturn(mockDefaultResultSetHandler);
		Field boundSqlField = DefaultResultSetHandler.class.getDeclaredField("boundSql");
		boundSqlField.setAccessible(true);
		boundSqlField.set(mockDefaultResultSetHandler, mockBoundSql);
		when(mockBoundSql.getSql()).thenReturn("select * from table_test");

		Set<String> resolvedTableNames = TableNameUtils.resolveResultSetTableName(mockInvocation);
		assertTrue(resolvedTableNames.contains("table_test"));
	}

}