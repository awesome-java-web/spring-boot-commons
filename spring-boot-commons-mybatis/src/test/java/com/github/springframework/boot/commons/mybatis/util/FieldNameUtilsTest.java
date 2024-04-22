package com.github.springframework.boot.commons.mybatis.util;

import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class FieldNameUtilsTest {

    @Test
    void testNewInstance() {
        UnsupportedOperationException e = assertThrows(UnsupportedOperationException.class, FieldNameUtils::new);
        assertEquals("Utility class should not be instantiated", e.getMessage());
    }

    @Test
    void testDetermineTargetFieldNames() {
        List<String> tableFields = Arrays.asList("user_id", "user_name", "user_age");
        List<String> result = FieldNameUtils.determineTargetFieldNames(Collections.singletonMap("*", tableFields), "test");
        assertEquals(tableFields, result);
        result = FieldNameUtils.determineTargetFieldNames(Collections.singletonMap("test", tableFields), "test");
        assertEquals(tableFields, result);
        result = FieldNameUtils.determineTargetFieldNames(Collections.singletonMap("test", tableFields), "test1");
        assertEquals(Collections.emptyList(), result);
    }

    @Test
    void testUnderscoreToCamelCase() {
        IllegalArgumentException e = assertThrows(IllegalArgumentException.class, () -> FieldNameUtils.underscoreToCamelCase(null));
        assertEquals("underscoreName == null || underscoreName.isEmpty()", e.getMessage());
        e = assertThrows(IllegalArgumentException.class, () -> FieldNameUtils.underscoreToCamelCase(""));
        assertEquals("underscoreName == null || underscoreName.isEmpty()", e.getMessage());

        assertEquals("userUTest", FieldNameUtils.underscoreToCamelCase("user_u_test"));
        assertEquals("userId", FieldNameUtils.underscoreToCamelCase("user_id"));
        assertEquals("userName", FieldNameUtils.underscoreToCamelCase("user_name"));
        assertEquals("userAge", FieldNameUtils.underscoreToCamelCase("user_age"));
    }

}