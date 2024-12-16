package com.github.awesome.springboot.commons.data.mybatis.support;

import org.apache.ibatis.type.LocalDateTypeHandler;

import java.sql.CallableStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.LocalDate;

/**
 * 自定义 MyBatis 的类型处理器，用于将数据库中的日期/时间类型（如{@link Timestamp}）转换为 Java 8 {@link LocalDate}类型。
 * <p>
 * 本类继承自{@link LocalDateTypeHandler}，并重写了相关方法，以便能够处理从数据库中读取的{@link ResultSet}或{@link CallableStatement}中的{@link LocalDate}类型数据。
 * </p>
 * <p>
 * 本类型处理器主要用于将数据库返回的{@link Timestamp}类型或{@link LocalDate}类型转换为 Java 8 {@link LocalDate}类型。
 * </p>
 *
 * <p>
 * 注意：该类型处理器是可选的，只有在 MyBatis 映射的字段需要转换为{@link LocalDate}时才会使用。
 * </p>
 *
 * @author <a href="https://github.com/codeboyzhou">codeboyzhou</a>
 * @see LocalDateTypeHandler
 * @since 1.0.5
 */
public class LocalDateTypeHandlerCustomizer extends LocalDateTypeHandler {

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

    /**
     * 将给定的数据库列值转换为{@link LocalDate}。
     * <p>
     * 如果列值是{@link Timestamp}类型，将其转换为{@link LocalDate}；如果列值本身是{@link LocalDate}类型，直接返回。
     * 如果列值是其他类型，则返回{@code null}。
     * </p>
     *
     * @param columnValue 从数据库中读取的列值。
     * @return 转换后的 LocalDate 对象，如果无法转换，返回{@code null}。
     */
    private LocalDate toLocalDate(Object columnValue) {
        if (columnValue instanceof Timestamp) {
            Timestamp timestamp = (Timestamp) columnValue;
            return timestamp.toLocalDateTime().toLocalDate();
        } else if (columnValue instanceof LocalDate) {
            return (LocalDate) columnValue;
        }
        return null;
    }

}
