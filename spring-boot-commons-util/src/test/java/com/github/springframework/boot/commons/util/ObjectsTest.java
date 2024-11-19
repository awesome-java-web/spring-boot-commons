package com.github.springframework.boot.commons.util;

import com.github.springframework.boot.commons.util.base.Chars;
import com.github.springframework.boot.commons.util.base.Objects;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.*;

class ObjectsTest {

    @Test
    void testNewInstance() {
        UnsupportedOperationException e = assertThrows(UnsupportedOperationException.class, Objects::new);
        assertEquals("Utility class should not be instantiated", e.getMessage());
    }

    @Test
    void testIsNotPrimitiveOrWrapper() {
        // false
        assertFalse(Objects.isNotPrimitiveOrWrapper(true));
        assertFalse(Objects.isNotPrimitiveOrWrapper((byte) 0));
        assertFalse(Objects.isNotPrimitiveOrWrapper(Chars.AT.charValue()));
        assertFalse(Objects.isNotPrimitiveOrWrapper((short) 0));
        assertFalse(Objects.isNotPrimitiveOrWrapper(0));
        assertFalse(Objects.isNotPrimitiveOrWrapper(0L));
        assertFalse(Objects.isNotPrimitiveOrWrapper(0.0F));
        assertFalse(Objects.isNotPrimitiveOrWrapper(0.0D));
        // true
        assertTrue(Objects.isNotPrimitiveOrWrapper("string"));
        assertTrue(Objects.isNotPrimitiveOrWrapper(new Object()));
        assertTrue(Objects.isNotPrimitiveOrWrapper(new TestClass()));
        assertTrue(Objects.isNotPrimitiveOrWrapper(new int[]{1, 2, 3}));
        assertTrue(Objects.isNotPrimitiveOrWrapper(new Integer[]{1, 2, 3}));
        assertTrue(Objects.isNotPrimitiveOrWrapper(new ArrayList<>()));
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
        List<Field> allFields = Objects.getAllFields(new TestClass());
        Set<String> allFieldNames = allFields.stream().map(Field::getName).collect(Collectors.toSet());
        assertTrue(allFieldNames.contains("name"));
        assertTrue(allFieldNames.contains("age"));
        assertTrue(allFieldNames.contains("address"));
        assertTrue(allFieldNames.contains("city"));
        assertTrue(allFieldNames.contains("province"));
        assertTrue(allFieldNames.contains("country"));
        assertTrue(Objects.getAllFields(new Object()).isEmpty());
    }

}
