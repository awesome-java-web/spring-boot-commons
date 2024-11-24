package com.github.springframework.boot.commons.util.base;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public final class Classes {

	private static final LRUMap<String, List<Field>> CLASS_FIELDS_CACHE = new LRUMap<>(100);

	public Classes() {
		throw new UnsupportedOperationException("Utility class should not be instantiated");
	}

	public static List<Field> getAllFieldsOf(Class<?> clazz) {
		List<Field> cachedFields = CLASS_FIELDS_CACHE.get(clazz.getName());
		if (cachedFields != null) {
			return cachedFields;
		}
		List<Field> allFields = new ArrayList<>();
		while (clazz != Object.class) {
			Field[] declaredFields = clazz.getDeclaredFields();
			allFields.addAll(Arrays.asList(declaredFields));
			clazz = clazz.getSuperclass();
		}
		CLASS_FIELDS_CACHE.put(clazz.getName(), allFields);
		return allFields;
	}

	public static Object newInstanceOf(Class<?> clazz) {
		try {
			return clazz.newInstance();
		} catch (InstantiationException | IllegalAccessException e) {
			return null;
		}
	}

}
