package com.github.awesome.springboot.commons.data.mybatis.interceptor;

import com.github.awesome.springboot.commons.base.Chars;
import com.github.awesome.springboot.commons.base.Strings;
import com.github.awesome.springboot.commons.data.mybatis.enums.SqlKeywords;
import com.google.common.base.CaseFormat;
import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import org.apache.ibatis.binding.MapperMethod;
import org.apache.ibatis.executor.resultset.DefaultResultSetHandler;
import org.apache.ibatis.mapping.BoundSql;
import org.apache.ibatis.mapping.MappedStatement;
import org.apache.ibatis.mapping.SqlCommandType;
import org.apache.ibatis.plugin.Interceptor;

import java.lang.reflect.Field;
import java.util.*;

import static java.util.stream.Collectors.toSet;

/**
 * MyBatis 拦截器的抽象类，提供了对 SQL 执行过程的拦截和自定义处理功能。
 * 该类主要提供了针对 SQL 语句解析、字段处理以及拦截的基本方法。
 * 具体的拦截器可以继承此类并实现特定的拦截逻辑。
 *
 * @author <a href="https://github.com/codeboyzhou">codeboyzhou</a>
 * @see Interceptor
 * @since 1.1.0
 */
public abstract class AbstractMybatisInterceptor implements Interceptor {

    /**
     * SQL 语句中的 BoundSql 字段名
     */
    private static final String FIELD_NAME_BOUND_SQL = "boundSql";

    /**
     * 用于匹配空白字符的正则表达式
     */
    private static final String REGEX_WHITESPACE = "\\s+";

    /**
     * 空字段数组，避免重复创建对象
     */
    private static final Field[] NO_FIELDS = {};

    /**
     * 用于缓存{@code Class}到它的字段数组的映射
     */
    private static final Cache<Class<?>, Field[]> declaredFieldsCache = CacheBuilder.newBuilder().initialCapacity(256).softValues().build();

    /**
     * 从{@link MappedStatement}中解析出 SQL 语句，并提取 SQL 中涉及到的表名。
     *
     * @param statement MyBatis 的{@link MappedStatement}，包含 SQL 语句的相关信息。
     * @param parameter 执行 SQL 时的参数。
     * @return 包含 SQL 中所有表名的 Set 集合。
     */
    protected Set<String> parseTableNamesFromSql(MappedStatement statement, Object parameter) {
        BoundSql boundSql = statement.getBoundSql(parameter);
        final String sql = boundSql.getSql();
        return parseTableNamesFromSql(sql);
    }

    /**
     * 从{@link DefaultResultSetHandler}中解析 SQL 语句，并提取 SQL 中涉及到的表名。
     *
     * @param resultSetHandler MyBatis 的{@link DefaultResultSetHandler}，用于处理 SQL 结果集的处理。
     * @return 包含 SQL 中所有表名的 Set 集合。
     * @throws NoSuchFieldException   如果未找到字段。
     * @throws IllegalAccessException 如果无法访问字段。
     */
    protected Set<String> parseTableNamesFromSql(DefaultResultSetHandler resultSetHandler) throws NoSuchFieldException, IllegalAccessException {
        Field boundSqlField = resultSetHandler.getClass().getDeclaredField(FIELD_NAME_BOUND_SQL);
        boundSqlField.setAccessible(true);
        Object boundSqlObject = boundSqlField.get(resultSetHandler);
        BoundSql boundSql = (BoundSql) boundSqlObject;
        final String sql = boundSql.getSql();
        return parseTableNamesFromSql(sql);
    }

    /**
     * 从给定的 SQL 语句中提取所有涉及到的表名。
     *
     * @param sql SQL 语句。
     * @return 包含 SQL 中所有表名的 Set 集合。
     */
    protected Set<String> parseTableNamesFromSql(final String sql) {
        Set<String> tableNames = new HashSet<>();
        String[] tokens = sql.split(REGEX_WHITESPACE);
        for (int i = 0; i < tokens.length; i++) {
            final boolean isIntoKeyword = SqlKeywords.INTO.name().equalsIgnoreCase(tokens[i]);
            final boolean isFromKeyword = SqlKeywords.FROM.name().equalsIgnoreCase(tokens[i]);
            final boolean isJoinKeyword = SqlKeywords.JOIN.name().equalsIgnoreCase(tokens[i]);
            final boolean isUpdateKeyword = SqlCommandType.UPDATE.name().equalsIgnoreCase(tokens[i]);
            if (isIntoKeyword || isFromKeyword || isJoinKeyword || isUpdateKeyword) {
                String tableName = tokens[i + 1];
                if (tableName.contains(Chars.BACKTICK.stringValue())) {
                    tableName = tableName.replace(Chars.BACKTICK.stringValue(), Strings.EMPTY);
                }
                tableNames.add(tableName);
            }
        }
        return tableNames;
    }

    /**
     * 根据不同的字段类型（{@link Map}、{@link Iterable}或自定义对象）对字段进行处理。
     *
     * @param handler                 {@link MybatisFieldHandler}用于处理字段的具体逻辑。
     * @param tableName               表名，指明当前处理字段所属的表。
     * @param sqlParameterOrResultSet Mybatis Mapper 方法的入参或返回值对象，可以是{@link Map}、{@link Iterable}或自定义对象。
     * @throws IllegalAccessException 如果无法访问字段时抛出此异常。
     */
    @SuppressWarnings("unchecked")
    protected void doIntercept(MybatisFieldHandler handler, final String tableName, Object sqlParameterOrResultSet) throws IllegalAccessException {
        if (sqlParameterOrResultSet instanceof Map) {
            resolveMap(handler, tableName, (Map<String, Object>) sqlParameterOrResultSet);
        } else if (sqlParameterOrResultSet instanceof Iterable) {
            resolveIterable(handler, tableName, (Iterable<?>) sqlParameterOrResultSet);
        } else if (isUserDefinedObject(sqlParameterOrResultSet)) {
            resolveUserDefinedObject(handler, tableName, sqlParameterOrResultSet);
        }
    }

    /**
     * 处理{@link Map}类型的字段值，根据不同的类型进行相应的处理。
     * 如果{@link Map}的{@link Map#values()}是{@link Iterable}类型，则会进一步处理。
     * 如果{@link Map}的值是普通的单一值，则根据表名和字段名进行匹配处理。
     *
     * @param handler   处理字段的具体逻辑，提供字段的处理方法。
     * @param tableName 表名，指明当前处理字段所属的表。
     * @param map       需要处理的{@link Map}，键为字段名，值为字段的值。
     * @throws IllegalAccessException 如果无法访问字段时抛出此异常。
     */
    private void resolveMap(MybatisFieldHandler handler, final String tableName, Map<String, Object> map) throws IllegalAccessException {
        // If map instanceof Map<K, Iterable<E>>
        final boolean mapValueIterable = map.values().iterator().next() instanceof Iterable;
        if (mapValueIterable) {
            // 如果 map 参数的实际类型是 org.apache.ibatis.binding.MapperMethod.ParamMap
            // 那么这个 map 里可能会有多个相同的值，但是这些值的 key 不同，这是 Mybatis 的实现机制
            // 导致的，所以这里对 map.values() 里面的元素做后续处理时需要先去重，避免重复处理
            final boolean isMybatisParamMap = map instanceof MapperMethod.ParamMap;
            Set<Object> distinctValues = Collections.singleton(map.values().iterator().next());
            resolveIterable(handler, tableName, isMybatisParamMap ? distinctValues : map.values());
        }
        // If map instanceof Map<K, V>
        else {
            Map<String, List<String>> targetTableFields = handler.targetTableFields();
            List<String> tableFieldNames = determineTargetFieldNames(targetTableFields, tableName);
            int tableFieldNameMatches = 0;
            for (String tableFieldName : tableFieldNames) {
                final String fieldCamelCaseName = CaseFormat.LOWER_UNDERSCORE.to(CaseFormat.LOWER_CAMEL, tableFieldName);
                // 这里不要直接使用 map.get() 方法，因为如果 map 的实际类型是
                // org.apache.ibatis.binding.MapperMethod.ParamMap
                // 直接使用 map.get() 方法会抛异常(Mybatis 重写过这个方法)
                if (map.containsKey(fieldCamelCaseName)) {
                    Object mapValue = map.get(fieldCamelCaseName);
                    Object handledValue = handler.handle(tableName, tableFieldName, mapValue);
                    map.put(fieldCamelCaseName, handledValue);
                    tableFieldNameMatches++;
                }
            }

            // 如果上面的逻辑没有匹配到任何字段名，那么这个 map.value() 里面可能装的是用户自定义的对象
            if (tableFieldNameMatches == 0) {
                Object userDefinedObject = map.values().iterator().next();
                resolveUserDefinedObject(handler, tableName, userDefinedObject);
            }
        }
    }

    /**
     * 判断给定的对象是否是用户自定义的对象。
     * 该方法通过检查对象所属的包名来判断是否是 Java 系统类或用户自定义类。
     *
     * @param object 需要判断的对象。
     * @return 如果是用户自定义对象，则返回{@code true}；否则返回{@code false}。
     */
    private boolean isUserDefinedObject(Object object) {
        if (object == null) {
            return false;
        }
        final String packageName = object.getClass().getPackage().getName();
        return !packageName.startsWith("java.") && !packageName.startsWith("javax.") && !packageName.startsWith("com.sun.");
    }

    /**
     * 根据表名从目标表字段映射中确定需要处理的字段名称。
     * 如果目标表字段映射中包含当前表名，则返回该表名对应的字段列表；
     * 如果没有，则返回通配符对应的字段列表，或者返回空列表。
     *
     * @param targetTableFields 目标表的字段映射，键为表名，值为字段名列表。
     * @param tableName         当前处理的表名。
     * @return 当前表名对应的字段名列表，如果没有找到则返回空列表或通配符对应的字段列表。
     */
    private List<String> determineTargetFieldNames(Map<String, List<String>> targetTableFields, final String tableName) {
        if (targetTableFields.containsKey(tableName)) {
            return targetTableFields.get(tableName);
        }
        return targetTableFields.getOrDefault(Chars.WILDCARD.stringValue(), Collections.emptyList());
    }

    /**
     * 递归处理{@link Iterable}类型的元素，根据元素的具体类型进行相应的处理。
     * 如果元素是{@link Map}类型，调用{@link #resolveMap}方法进行处理；
     * 如果元素是{@link Iterable}类型，递归调用方法本身进行处理；
     * 如果元素是用户自定义对象，调用{@link #resolveUserDefinedObject}方法进行处理。
     *
     * @param handler   处理字段的具体逻辑，提供字段的处理方法。
     * @param tableName 当前处理字段所属的表名。
     * @param iterable  需要处理的{@link Iterable}对象，包含多个元素。
     * @throws IllegalAccessException 如果无法访问字段时抛出此异常。
     */
    @SuppressWarnings("unchecked")
    private void resolveIterable(MybatisFieldHandler handler, final String tableName, Iterable<?> iterable) throws IllegalAccessException {
        for (Object element : iterable) {
            // 如果 element instanceof Map<K, V>
            if (element instanceof Map) {
                resolveMap(handler, tableName, (Map<String, Object>) element);
            }
            // 如果 element instanceof Iterable<E>
            else if (element instanceof Iterable) {
                resolveIterable(handler, tableName, (Iterable<?>) element);
            }
            // 如果 element 是用户自定义的 Java 对象
            else if (isUserDefinedObject(element)) {
                resolveUserDefinedObject(handler, tableName, element);
            }
        }
    }

    /**
     * 处理用户自定义 Java 对象的字段。
     * 根据目标表的字段名称，匹配对象中的字段并处理其值。
     * 该方法会遍历对象的所有字段，如果字段名称匹配，
     * 使用{@link MybatisFieldHandler#handle(String, String, Object)}方法进行处理。
     *
     * @param handler   处理字段的具体逻辑，提供字段的处理方法。
     * @param tableName 当前处理字段所属的表名。
     * @param object    需要处理的用户自定义对象。
     * @throws IllegalAccessException 如果无法访问字段时抛出此异常。
     */
    private void resolveUserDefinedObject(MybatisFieldHandler handler, final String tableName, Object object) throws IllegalAccessException {
        Map<String, List<String>> targetTableFields = handler.targetTableFields();
        List<String> tableFieldNames = determineTargetFieldNames(targetTableFields, tableName);
        if (tableFieldNames == null || tableFieldNames.isEmpty()) {
            return;
        }

        Set<String> javaFieldNames = tableFieldNames.stream()
            .map(tableFieldName -> CaseFormat.LOWER_UNDERSCORE.to(CaseFormat.LOWER_CAMEL, tableFieldName))
            .collect(toSet());

        // 反射获取对象的所有字段，包括父类的字段
        Class<?> targetClass = object.getClass();
        do {
            Field[] fields = getDeclaredFields(targetClass);
            for (Field field : fields) {
                if (javaFieldNames.contains(field.getName())) {
                    field.setAccessible(true);
                    Object javaFieldValue = field.get(object);
                    Object handledValue = handler.handle(tableName, field.getName(), javaFieldValue);
                    field.set(object, handledValue);
                }
            }
            targetClass = targetClass.getSuperclass();
        }
        while (targetClass != null && targetClass != Object.class);
    }

    /**
     * 获取类的所有声明字段。
     * 如果缓存中有该类的字段，则直接返回缓存的结果；
     * 否则，通过反射获取字段并缓存，以提高性能。
     *
     * @param clazz 需要获取字段的类。
     * @return 类的所有声明字段。
     * @throws IllegalStateException 如果获取字段失败时抛出此异常。
     */
    private Field[] getDeclaredFields(Class<?> clazz) {
        Field[] result = declaredFieldsCache.getIfPresent(clazz);
        if (result == null) {
            try {
                result = clazz.getDeclaredFields();
                declaredFieldsCache.put(clazz, (result.length == 0 ? NO_FIELDS : result));
            } catch (Throwable e) {
                final String error = String.format("Failed to introspect Class [%s] from ClassLoader [%s]", clazz.getName(), clazz.getClassLoader());
                throw new IllegalStateException(error, e);
            }
        }
        return result;
    }

}
