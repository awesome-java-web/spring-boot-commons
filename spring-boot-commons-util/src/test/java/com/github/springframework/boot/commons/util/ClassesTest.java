package com.github.springframework.boot.commons.util;

import com.github.springframework.boot.commons.util.base.Classes;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Field;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.*;

class ClassesTest {

	@Test
	void testNewInstance() {
		UnsupportedOperationException e = assertThrows(UnsupportedOperationException.class, Classes::new);
		assertEquals("Utility class should not be instantiated", e.getMessage());
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
		List<Field> allFields = Classes.getAllFieldsOf(TestClass.class);
		Set<String> allFieldNames = allFields.stream().map(Field::getName).collect(Collectors.toSet());
		assertTrue(allFieldNames.contains("name"));
		assertTrue(allFieldNames.contains("age"));
		assertTrue(allFieldNames.contains("address"));
		assertTrue(allFieldNames.contains("city"));
		assertTrue(allFieldNames.contains("province"));
		assertTrue(allFieldNames.contains("country"));
		assertTrue(Classes.getAllFieldsOf(Object.class).isEmpty());
	}

}
