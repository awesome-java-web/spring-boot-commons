package com.github.springframework.boot.commons.mybatis.interceptor;

import com.github.springframework.boot.commons.mybatis.handler.MybatisFieldHandler;
import com.github.springframework.boot.commons.mybatis.util.FieldNameUtils;
import org.apache.ibatis.binding.MapperMethod;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.ReflectionUtils;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Set;

import static java.util.stream.Collectors.toSet;

class ParameterAndResultSetResolver {

    private static final Logger logger = LoggerFactory.getLogger(ParameterAndResultSetResolver.class);

    @SuppressWarnings("unchecked")
    public void resolve(MybatisFieldHandler handler, final String tableName, Object value) throws IllegalAccessException {
        if (value instanceof Map) {
            resolveMap(handler, tableName, (Map<String, Object>) value);
        } else if (value instanceof Iterable) {
            resolveIterable(handler, tableName, (Iterable<?>) value);
        } else if (isUserDefinedObject(value)) {
            resolveUserDefinedObject(handler, tableName, value);
        }
    }

    private void resolveMap(MybatisFieldHandler handler, final String tableName, Map<String, Object> map) throws IllegalAccessException {
        // If map instanceof Map<K, Iterable<E>>
        final boolean mapValueIterable = map.values().iterator().next() instanceof Iterable;
        if (mapValueIterable) {
            if (logger.isDebugEnabled()) {
                logger.debug("The map.values() is iterable for table '{}', use 'handleIterable' method to proceed", tableName);
            }
            // If the map instanceof org.apache.ibatis.binding.MapperMethod.ParamMap, there could be
            // multiple same values but with different keys in the map, so we need to get distinct
            // values to avoid duplicate handling.
            final boolean isMybatisParamMap = map instanceof MapperMethod.ParamMap;
            Set<Object> distinctValues = Collections.singleton(map.values().iterator().next());
            resolveIterable(handler, tableName, isMybatisParamMap ? distinctValues : map.values());
        }
        // If map instanceof Map<K, V>
        else {
            Map<String, List<String>> targetTableFields = handler.targetTableFields();
            List<String> tableFieldNames = FieldNameUtils.determineTargetFieldNames(targetTableFields, tableName);
            if (logger.isDebugEnabled()) {
                logger.debug("The map.values() is not iterable for table '{}', will be handled as typical Map<K,V> with target table fields {}",
                    tableName, tableFieldNames
                );
            }

            int tableFieldNameMatches = 0;
            for (String tableFieldName : tableFieldNames) {
                final String fieldCamelCaseName = FieldNameUtils.underscoreToCamelCase(tableFieldName);
                // Do not use map.get() method directly here, because if the map instanceof
                // org.apache.ibatis.binding.MapperMethod.ParamMap, it will throw an exception.
                if (map.containsKey(fieldCamelCaseName)) {
                    Object mapValue = map.get(fieldCamelCaseName);
                    Object handledValue = handler.handle(tableName, tableFieldName, mapValue);
                    map.put(fieldCamelCaseName, handledValue);
                    tableFieldNameMatches++;
                }
            }

            // If no table field name matches, it means the map is not a typical table field map.
            if (tableFieldNameMatches == 0) {
                Object userDefinedObject = map.values().iterator().next();
                resolveUserDefinedObject(handler, tableName, userDefinedObject);
            }
        }
    }

    @SuppressWarnings("unchecked")
    private void resolveIterable(MybatisFieldHandler handler, final String tableName, Iterable<?> iterable) throws IllegalAccessException {
        for (Object element : iterable) {
            if (element instanceof Map) {
                if (logger.isDebugEnabled()) {
                    logger.debug("Ths inner element is instance of Map, use 'resolveMap' method to proceed");
                }
                resolveMap(handler, tableName, (Map<String, Object>) element);
            } else if (element instanceof Iterable) {
                if (logger.isDebugEnabled()) {
                    logger.debug("Ths inner element is instance of Iterable, use 'resolveIterable' method to proceed");
                }
                resolveIterable(handler, tableName, (Iterable<?>) element);
            } else if (isUserDefinedObject(element)) {
                if (logger.isDebugEnabled()) {
                    logger.debug("The inner element is user defined Java object, use 'resolveUserDefinedObject' method to proceed");
                }
                resolveUserDefinedObject(handler, tableName, element);
            }
        }
    }

    private void resolveUserDefinedObject(MybatisFieldHandler handler, final String tableName, Object object) throws IllegalAccessException {
        Map<String, List<String>> targetTableFields = handler.targetTableFields();
        List<String> tableFieldNames = FieldNameUtils.determineTargetFieldNames(targetTableFields, tableName);

        if (logger.isDebugEnabled()) {
            logger.debug("Fields {} of table '{}' will be handled for user defined Java object {}", tableFieldNames, tableName, object);
        }

        if (tableFieldNames == null || tableFieldNames.isEmpty()) {
            if (logger.isDebugEnabled()) {
                logger.debug("No fields need to be handled for table '{}', skip to proceed", tableName);
            }
            return;
        }

        Set<String> javaFieldNames = tableFieldNames.stream().map(FieldNameUtils::underscoreToCamelCase).collect(toSet());
        ReflectionUtils.doWithFields(object.getClass(), field -> {
            if (javaFieldNames.contains(field.getName())) {
                field.setAccessible(true);
                Object javaFieldValue = field.get(object);
                Object handledValue = handler.handle(tableName, field.getName(), javaFieldValue);
                field.set(object, handledValue);
            }
        });
    }

    private static boolean isUserDefinedObject(Object object) {
        if (object == null) {
            return false;
        }
        final String packageName = object.getClass().getPackage().getName();
        return !packageName.startsWith("java.") && !packageName.startsWith("javax.") && !packageName.startsWith("com.sun.");
    }

}
