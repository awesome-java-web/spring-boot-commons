package com.github.springframework.boot.commons.util;

import org.junit.jupiter.api.Test;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.*;

class ObjectUtilsTest {

    @Test
    void testNewInstance() {
        UnsupportedOperationException e = assertThrows(UnsupportedOperationException.class, ObjectUtils::new);
        assertEquals("Utility class should not be instantiated", e.getMessage());
    }

    @Test
    void testIsNotPrimitiveOrWrapper() {
        // false
        assertFalse(ObjectUtils.isNotPrimitiveOrWrapper(true));
        assertFalse(ObjectUtils.isNotPrimitiveOrWrapper((byte) 0));
        assertFalse(ObjectUtils.isNotPrimitiveOrWrapper(Chars.AT.charValue()));
        assertFalse(ObjectUtils.isNotPrimitiveOrWrapper((short) 0));
        assertFalse(ObjectUtils.isNotPrimitiveOrWrapper(0));
        assertFalse(ObjectUtils.isNotPrimitiveOrWrapper(0L));
        assertFalse(ObjectUtils.isNotPrimitiveOrWrapper(0.0F));
        assertFalse(ObjectUtils.isNotPrimitiveOrWrapper(0.0D));
        // true
        assertTrue(ObjectUtils.isNotPrimitiveOrWrapper("string"));
        assertTrue(ObjectUtils.isNotPrimitiveOrWrapper(new Object()));
        assertTrue(ObjectUtils.isNotPrimitiveOrWrapper(new TestClass()));
        assertTrue(ObjectUtils.isNotPrimitiveOrWrapper(new int[]{1, 2, 3}));
        assertTrue(ObjectUtils.isNotPrimitiveOrWrapper(new Integer[]{1, 2, 3}));
        assertTrue(ObjectUtils.isNotPrimitiveOrWrapper(new ArrayList<>()));
    }

    static class TestSuperClass {

        private String name;

        private int age;

    }

    static class TestClass extends TestSuperClass {

        private String address;

        private String city;

        private String province;

        private String country;

    }

    @Test
    void testGetAllFields() {
        List<Field> allFields = ObjectUtils.getAllFields(new TestClass());
        Set<String> allFieldNames = allFields.stream().map(Field::getName).collect(Collectors.toSet());
        assertTrue(allFieldNames.contains("name"));
        assertTrue(allFieldNames.contains("age"));
        assertTrue(allFieldNames.contains("address"));
        assertTrue(allFieldNames.contains("city"));
        assertTrue(allFieldNames.contains("province"));
        assertTrue(allFieldNames.contains("country"));
        assertTrue(ObjectUtils.getAllFields(new Object()).isEmpty());
    }

}