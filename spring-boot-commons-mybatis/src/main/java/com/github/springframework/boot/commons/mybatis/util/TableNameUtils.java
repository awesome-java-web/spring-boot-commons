package com.github.springframework.boot.commons.mybatis.util;

import org.apache.ibatis.executor.resultset.DefaultResultSetHandler;
import org.apache.ibatis.mapping.BoundSql;
import org.apache.ibatis.mapping.MappedStatement;
import org.apache.ibatis.mapping.SqlCommandType;
import org.apache.ibatis.plugin.Invocation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Field;

public final class TableNameUtils {

    private static final Logger logger = LoggerFactory.getLogger(TableNameUtils.class);

    private static final String FIELD_NAME_BOUND_SQL = "boundSql";

    private static final String REGEX_WHITESPACE = "\\s+";

    private static final String SQL_KEYWORD_INTO = "INTO";

    private static final String SQL_KEYWORD_FROM = "FROM";

    TableNameUtils() {
        throw new UnsupportedOperationException("Utility class should not be instantiated");
    }

    public static String resolveExecutorTableName(Invocation invocation) {
        Object[] args = invocation.getArgs();
        MappedStatement statement = (MappedStatement) args[0];
        Object parameter = args[1];
        BoundSql boundSql = statement.getBoundSql(parameter);
        final String sql = boundSql.getSql();
        return resolveTableName(sql);
    }

    public static String resolveResultSetTableName(Invocation invocation) throws NoSuchFieldException, IllegalAccessException {
        Object target = invocation.getTarget();
        DefaultResultSetHandler defaultResultSetHandler = (DefaultResultSetHandler) target;
        Field boundSqlField = defaultResultSetHandler.getClass().getDeclaredField(FIELD_NAME_BOUND_SQL);
        boundSqlField.setAccessible(true);
        Object boundSqlObject = boundSqlField.get(defaultResultSetHandler);
        BoundSql boundSql = (BoundSql) boundSqlObject;
        final String sql = boundSql.getSql();
        return resolveTableName(sql);
    }

    public static String resolveTableName(final String sql) {
        String tableName = null;
        String[] tokens = sql.split(REGEX_WHITESPACE);
        for (int i = 0; i < tokens.length; i++) {
            final boolean isIntoKeyword = SQL_KEYWORD_INTO.equalsIgnoreCase(tokens[i]);
            final boolean isFromKeyword = SQL_KEYWORD_FROM.equalsIgnoreCase(tokens[i]);
            final boolean isUpdateKeyword = SqlCommandType.UPDATE.name().equalsIgnoreCase(tokens[i]);
            if (isIntoKeyword || isFromKeyword || isUpdateKeyword) {
                tableName = tokens[i + 1];
                break;
            }
        }
        if (logger.isDebugEnabled()) {
            logger.debug("Resolved table name '{}' from sql: {}", tableName, sql);
        }
        return tableName;
    }

}
