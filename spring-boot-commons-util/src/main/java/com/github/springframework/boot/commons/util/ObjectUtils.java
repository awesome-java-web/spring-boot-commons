package com.github.springframework.boot.commons.util;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public final class ObjectUtils {

    ObjectUtils() {
        throw new UnsupportedOperationException("Utility class should not be instantiated");
    }

    public static boolean isNotPrimitiveOrWrapper(Object object) {
        Class<?> clazz = object.getClass();
        return clazz != Boolean.class && clazz != Byte.class
            && clazz != Character.class && clazz != Short.class
            && clazz != Integer.class && clazz != Long.class
            && clazz != Float.class && clazz != Double.class;
    }

    public static List<Field> getAllFields(Object object) {
        List<Field> allFields = new ArrayList<>();
        Class<?> searchClass = object.getClass();
        while (searchClass != Object.class) {
            Field[] declaredFields = searchClass.getDeclaredFields();
            allFields.addAll(Arrays.asList(declaredFields));
            searchClass = searchClass.getSuperclass();
        }
        return allFields;
    }

}
