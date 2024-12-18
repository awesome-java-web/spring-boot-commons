package com.github.awesome.springboot.commons.data.mybatis.interceptor;

import java.util.List;
import java.util.Map;

/**
 * MyBatis 字段处理器接口，用于在 MyBatis 执行 SQL 操作时，对特定表的字段进行自定义处理。
 * <p>
 * 该接口允许开发者实现字段的自定义转换逻辑，在数据存储到数据库之前或从数据库读取后，进行数据的处理。
 * </p>
 *
 * <p>
 * 示例场景包括：字段值加密/解密、格式化、类型转换等。
 * </p>
 *
 * @author <a href="https://github.com/codeboyzhou">codeboyzhou</a>
 * @since 1.1.0
 */
public interface MybatisFieldHandler {

    /**
     * 处理指定表的字段值。
     * <p>
     * 该方法用于根据给定的表名、字段名及字段值，进行自定义的字段处理。处理后的字段值将被返回。
     * </p>
     * <p>
     * 例如，可以在这里进行字段的加密、解密、转换格式等操作。
     * </p>
     *
     * @param tableName  表名，用于指定该字段所在的数据库表。
     * @param fieldName  字段名，表示需要处理的字段。
     * @param fieldValue 字段值，表示要处理的具体值。该值可以是任何类型，具体类型由实际字段的类型决定。
     * @return 处理后的字段值。返回值的类型应与数据库中该字段的类型一致。
     */
    Object handle(final String tableName, final String fieldName, Object fieldValue);

    /**
     * 获取当前处理器需要处理的目标数据表及其数据库字段的映射关系。
     * <p>
     * 该方法用于返回一个映射关系，表示该字段处理器负责处理哪些表的哪些字段。
     * 返回的{@link Map}中，键是表名，值是该表需要处理的字段列表。
     * </p>
     *
     * @return 一个Map，键为表名，值为该表的字段名列表。
     */
    Map<String, List<String>> targetTableFields();

}
