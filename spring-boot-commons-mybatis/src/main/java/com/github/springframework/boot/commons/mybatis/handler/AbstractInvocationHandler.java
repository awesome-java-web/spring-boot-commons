package com.github.springframework.boot.commons.mybatis.handler;

import com.github.springframework.boot.commons.mybatis.util.FieldNameUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public abstract class AbstractInvocationHandler implements MybatisInvocationHandler {

    private static final Logger logger = LoggerFactory.getLogger(AbstractInvocationHandler.class);

    @SuppressWarnings("unchecked")
    protected void handle(MybatisFieldHandler fieldHandler, final String tableName, Object value) throws IllegalAccessException {
        if (value instanceof Map) {
            handleMap(fieldHandler, tableName, (Map<String, Object>) value);
        } else if (value instanceof Iterable) {
            handleIterable(fieldHandler, tableName, (Iterable<?>) value);
        } else if (value != null && !value.getClass().isPrimitive()) {
            handleUserDefinedObject(fieldHandler, tableName, value);
        }
    }

    private void handleMap(MybatisFieldHandler fieldHandler, final String tableName, Map<String, Object> map) throws IllegalAccessException {
        // If map instanceof Map<K, Iterable<E>>
        final boolean mapValueIterable = map.values().iterator().next() instanceof Iterable;
        if (mapValueIterable) {
            if (logger.isDebugEnabled()) {
                logger.debug("The map.values() is iterable for table '{}', use 'handleIterable' method to continue handling", tableName);
            }
            handleIterable(fieldHandler, tableName, map.values());
        }
        // If map instanceof Map<K, V>
        else {
            Map<String, List<String>> targetTableFields = fieldHandler.targetTableFields();
            List<String> tableFieldNames = FieldNameUtils.determineTargetFieldNames(targetTableFields, tableName);
            if (logger.isDebugEnabled()) {
                logger.debug("The map.values() is not iterable for table '{}', will be handled as typically Map<K,V> with target table fields {}",
                    tableName, tableFieldNames
                );
            }
            for (String tableFieldName : tableFieldNames) {
                final String fieldCamelCaseName = FieldNameUtils.underscoreToCamelCase(tableFieldName);
                // Do not use map.get() method directly here, because if the map instanceof
                // org.apache.ibatis.binding.MapperMethod.ParamMap, it will throw an exception.
                if (map.containsKey(fieldCamelCaseName)) {
                    Object mapValue = map.get(fieldCamelCaseName);
                    Object handledValue = fieldHandler.handle(tableName, tableFieldName, mapValue);
                    map.put(fieldCamelCaseName, handledValue);
                }
            }
        }
    }

    @SuppressWarnings("unchecked")
    private void handleIterable(MybatisFieldHandler fieldHandler, final String tableName, Iterable<?> iterable) throws IllegalAccessException {
        for (Object element : iterable) {
            if (element instanceof Map) {
                if (logger.isDebugEnabled()) {
                    logger.debug("Ths inner element is instance of Map, use 'handleMap' method to continue handling");
                }
                handleMap(fieldHandler, tableName, (Map<String, Object>) element);
            } else if (element instanceof Iterable) {
                if (logger.isDebugEnabled()) {
                    logger.debug("Ths inner element is instance of Iterable, use 'handleIterable' method to continue handling");
                }
                handleIterable(fieldHandler, tableName, (Iterable<?>) element);
            } else if (!element.getClass().isPrimitive()) {
                if (logger.isDebugEnabled()) {
                    logger.debug("The inner element is user defined Java object, use 'handleUserDefinedObject' method to continue handling");
                }
                handleUserDefinedObject(fieldHandler, tableName, element);
            }
        }
    }

    private void handleUserDefinedObject(MybatisFieldHandler fieldHandler, final String tableName, Object object) throws IllegalAccessException {
        Map<String, List<String>> targetTableFields = fieldHandler.targetTableFields();
        List<String> tableFieldNames = FieldNameUtils.determineTargetFieldNames(targetTableFields, tableName);

        if (logger.isDebugEnabled()) {
            logger.debug("Fields {} of table '{}' will be handled for user defined Java object {}",
                tableFieldNames, tableName, object.getClass().getName()
            );
        }

        if (tableFieldNames == null || tableFieldNames.isEmpty()) {
            if (logger.isDebugEnabled()) {
                logger.debug("No fields need to be handled for table '{}', skip handling", tableName);
            }
            return;
        }

        Field[] javaFields = object.getClass().getDeclaredFields();
        Map<String, Field> javaFieldMap = Arrays.stream(javaFields).collect(Collectors.toMap(Field::getName, field -> field));
        for (String tableFieldName : tableFieldNames) {
            final String javaFieldName = FieldNameUtils.underscoreToCamelCase(tableFieldName);
            Field javaField = javaFieldMap.get(javaFieldName);
            if (javaField == null) {
                if (logger.isDebugEnabled()) {
                    logger.debug("Java field '{}' is not found in object {}, skip handling", javaFieldName, object.getClass().getName());
                }
                continue;
            }
            javaField.setAccessible(true);
            Object javaFieldValue = javaField.get(object);
            Object handledValue = fieldHandler.handle(tableName, tableFieldName, javaFieldValue);
            javaField.set(object, handledValue);
        }
    }

}
