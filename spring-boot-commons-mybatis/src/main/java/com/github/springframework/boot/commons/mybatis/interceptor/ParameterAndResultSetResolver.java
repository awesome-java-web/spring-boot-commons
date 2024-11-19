package com.github.springframework.boot.commons.mybatis.interceptor;

import com.github.springframework.boot.commons.mybatis.handler.MybatisFieldHandler;
import com.github.springframework.boot.commons.mybatis.util.FieldNameUtils;
import com.github.springframework.boot.commons.util.base.Objects;
import org.apache.ibatis.binding.MapperMethod;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Field;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Set;

import static java.util.stream.Collectors.toMap;

class ParameterAndResultSetResolver {

	private static final Logger logger = LoggerFactory.getLogger(ParameterAndResultSetResolver.class);

	@SuppressWarnings("unchecked")
	public void resolve(MybatisFieldHandler handler, final String tableName, Object value) throws IllegalAccessException {
		if (value instanceof Map) {
			resolveMap(handler, tableName, (Map<String, Object>) value);
		} else if (value instanceof Iterable) {
			resolveIterable(handler, tableName, (Iterable<?>) value);
		} else if (value != null && Objects.isNotPrimitiveOrWrapper(value)) {
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
					logger.debug("Ths inner element is instance of Map, use 'handleMap' method to proceed");
				}
				resolveMap(handler, tableName, (Map<String, Object>) element);
			} else if (element instanceof Iterable) {
				if (logger.isDebugEnabled()) {
					logger.debug("Ths inner element is instance of Iterable, use 'handleIterable' method to proceed");
				}
				resolveIterable(handler, tableName, (Iterable<?>) element);
			} else if (element != null && Objects.isNotPrimitiveOrWrapper(element)) {
				if (logger.isDebugEnabled()) {
					logger.debug("The inner element is user defined Java object, use 'handleUserDefinedObject' method to proceed");
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

        List<Field> javaFields = Objects.getAllFields(object);
		Map<String, Field> javaFieldMap = javaFields.stream().collect(toMap(Field::getName, field -> field, (existing, replacement) -> existing));
		for (String tableFieldName : tableFieldNames) {
			final String javaFieldName = FieldNameUtils.underscoreToCamelCase(tableFieldName);
			Field javaField = javaFieldMap.get(javaFieldName);
			if (javaField == null) {
				if (logger.isDebugEnabled()) {
					logger.debug("Java field '{}' is not found in object {}, skip to proceed", javaFieldName, object);
				}
				continue;
			}
			javaField.setAccessible(true);
			Object javaFieldValue = javaField.get(object);
			Object handledValue = handler.handle(tableName, tableFieldName, javaFieldValue);
			javaField.set(object, handledValue);
		}
	}

}
