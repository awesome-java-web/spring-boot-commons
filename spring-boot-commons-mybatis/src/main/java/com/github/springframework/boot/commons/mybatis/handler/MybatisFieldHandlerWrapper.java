package com.github.springframework.boot.commons.mybatis.handler;

import com.github.springframework.boot.commons.mybatis.util.FieldNameUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class MybatisFieldHandlerWrapper {

    private static final Logger logger = LoggerFactory.getLogger(MybatisFieldHandlerWrapper.class);

    @SuppressWarnings("unchecked")
    public void doHandle(List<? extends MybatisFieldHandler> handlerChain, final String sqlTableName, Object object) throws IllegalAccessException {
        for (MybatisFieldHandler handler : handlerChain) {
            if (logger.isDebugEnabled()) {
                logger.debug("{} start handling table '{}'", handler.getClass().getSimpleName(), sqlTableName);
            }
            if (object instanceof Map) {
                handleMap(handler, sqlTableName, (Map<String, Object>) object);
            } else if (object instanceof Collection) {
                handleCollection(handler, sqlTableName, (Collection<?>) object);
            } else if (object != null) {
                handleObject(handler, sqlTableName, object);
            }
        }
    }

    private void handleMap(MybatisFieldHandler handler, final String sqlTableName, Map<String, Object> map) throws IllegalAccessException {
        // for data structure Map<K, List<E>>
        final boolean mapValueTypeIsCollection = map.values().iterator().next() instanceof Collection;
        if (mapValueTypeIsCollection) {
            if (logger.isDebugEnabled()) {
                logger.debug("Map<K, List<E>> data structure detected, nested collection will be handled for table '{}'", sqlTableName);
            }
            handleCollection(handler, sqlTableName, map.values());
        }
        // for data structure Map<K, V>
        else {
            Map<String, List<String>> targetTableFields = handler.targetTableFields();
            List<String> tableFieldNames = FieldNameUtils.determineTargetFieldNames(targetTableFields, sqlTableName);
            if (logger.isDebugEnabled()) {
                logger.debug("Map<K, V> data structure detected, fields {} of table '{}' will be handled", tableFieldNames, sqlTableName);
            }
            for (String tableFieldName : tableFieldNames) {
                final String fieldCamelCaseName = FieldNameUtils.underscoreToCamelCase(tableFieldName);
                // Do not use map.get() method directly here, because if the map instanceof
                // org.apache.ibatis.binding.MapperMethod.ParamMap, it will throw an exception.
                if (map.containsKey(fieldCamelCaseName)) {
                    Object mapValue = map.get(fieldCamelCaseName);
                    Object handledValue = handler.handle(sqlTableName, tableFieldName, mapValue);
                    map.put(fieldCamelCaseName, handledValue);
                }
            }
        }
    }

    @SuppressWarnings("unchecked")
    private void handleCollection(MybatisFieldHandler handler, final String sqlTableName, Collection<?> collection) throws IllegalAccessException {
        for (Object element : collection) {
            if (element instanceof Map) {
                if (logger.isDebugEnabled()) {
                    logger.debug("Nested Map<K, V> data structure detected in the collection, use 'handleMap' method to continue");
                }
                handleMap(handler, sqlTableName, (Map<String, Object>) element);
            } else if (element instanceof Collection) {
                if (logger.isDebugEnabled()) {
                    logger.debug("Nested Collection<E> data structure detected in the collection, use 'handleCollection' method to continue");
                }
                handleCollection(handler, sqlTableName, (Collection<?>) element);
            } else {
                if (logger.isDebugEnabled()) {
                    logger.debug("The element is ordinary Java object in the collection, use 'handleObject' method to continue");
                }
                handleObject(handler, sqlTableName, element);
            }
        }
    }

    private void handleObject(MybatisFieldHandler handler, final String sqlTableName, Object object) throws IllegalAccessException {
        Map<String, List<String>> targetTableFields = handler.targetTableFields();
        List<String> tableFieldNames = FieldNameUtils.determineTargetFieldNames(targetTableFields, sqlTableName);
        if (logger.isDebugEnabled()) {
            logger.debug("Fields {} of table '{}' will be handled for ordinary Java object {}", tableFieldNames, sqlTableName, object.getClass().getName());
        }

        if (!tableFieldNames.isEmpty()) {
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
                Object handledValue = handler.handle(sqlTableName, tableFieldName, javaFieldValue);
                javaField.set(object, handledValue);
            }
        }
    }

}
