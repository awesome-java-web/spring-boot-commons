package com.github.springframework.boot.commons.mybatis.support;

import org.apache.ibatis.type.LocalDateTimeTypeHandler;

import java.sql.CallableStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.LocalDateTime;

public class LocalDateTimeTypeHandlerSupport extends LocalDateTimeTypeHandler {

    @Override
    public LocalDateTime getNullableResult(ResultSet rs, String columnName) throws SQLException {
        Object columnValue = rs.getObject(columnName);
        return toLocalDateTime(columnValue);
    }

    @Override
    public LocalDateTime getNullableResult(ResultSet rs, int columnIndex) throws SQLException {
        Object columnValue = rs.getObject(columnIndex);
        return toLocalDateTime(columnValue);
    }

    @Override
    public LocalDateTime getNullableResult(CallableStatement cs, int columnIndex) throws SQLException {
        Object columnValue = cs.getObject(columnIndex);
        return toLocalDateTime(columnValue);
    }

    private LocalDateTime toLocalDateTime(Object columnValue) {
        if (columnValue instanceof java.sql.Timestamp) {
            java.sql.Timestamp timestamp = (Timestamp) columnValue;
            return timestamp.toLocalDateTime();
        }
        return null;
    }

}
