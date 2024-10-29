package com.github.springframework.boot.commons.mybatis.support;

import org.apache.ibatis.type.LocalDateTypeHandler;

import java.sql.CallableStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.LocalDate;

public class LocalDateTypeHandlerSupport extends LocalDateTypeHandler {

    @Override
    public LocalDate getNullableResult(ResultSet rs, String columnName) throws SQLException {
        Object columnValue = rs.getObject(columnName);
        return toLocalDate(columnValue);
    }

    @Override
    public LocalDate getNullableResult(ResultSet rs, int columnIndex) throws SQLException {
        Object columnValue = rs.getObject(columnIndex);
        return toLocalDate(columnValue);
    }

    @Override
    public LocalDate getNullableResult(CallableStatement cs, int columnIndex) throws SQLException {
        Object columnValue = cs.getObject(columnIndex);
        return toLocalDate(columnValue);
    }

    private LocalDate toLocalDate(Object columnValue) {
        if (columnValue instanceof Timestamp) {
            Timestamp timestamp = (Timestamp) columnValue;
            return timestamp.toLocalDateTime().toLocalDate();
        }
        return null;
    }

}
