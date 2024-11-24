package com.github.springframework.boot.commons.groovy;

import com.github.springframework.boot.commons.groovy.exception.GroovyObjectInvokeMethodException;
import com.github.springframework.boot.commons.groovy.exception.GroovyScriptParseException;
import com.github.springframework.boot.commons.groovy.exception.InvalidGroovyScriptException;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.platform.commons.util.ClassLoaderUtils.getClassLoader;

class GroovyScriptExecutorTest {

	static GroovyScriptExecutor groovyScriptExecutor;

	static String testNormalGroovyScript;

	static String readGroovyScriptAsString(final String fileName) throws IOException {
		ClassLoader classLoader = getClassLoader(GroovyScriptExecutorTest.class);
		try (InputStream inputStream = classLoader.getResourceAsStream(fileName)) {
			assert inputStream != null;
			InputStreamReader streamReader = new InputStreamReader(inputStream);
			BufferedReader bufferedReader = new BufferedReader(streamReader);
			return bufferedReader.lines().collect(Collectors.joining("\n"));
		}
	}

	@BeforeAll
	static void setup() throws IOException {
		groovyScriptExecutor = new GroovyScriptExecutor();
		testNormalGroovyScript = readGroovyScriptAsString("TestGroovyScriptExecutor.groovy");
	}

	@Test
	void testGroovyScriptNullOrEmpty() {
		// null
		InvalidGroovyScriptException nullException = assertThrows(InvalidGroovyScriptException.class,
			() -> groovyScriptExecutor.execute(null, "test", 1)
		);
		assertEquals("Groovy script is null", nullException.getMessage());
		// empty
		InvalidGroovyScriptException emptyException = assertThrows(InvalidGroovyScriptException.class,
			() -> groovyScriptExecutor.execute("", "test", 1)
		);
		assertEquals("Groovy script is empty", emptyException.getMessage());
	}

	@Test
	void testGroovyScriptInvokeMethodNoArgs() {
		Object result = groovyScriptExecutor.execute(testNormalGroovyScript, "testInvokeMethodNoArgs");
		assertEquals(1024, result);
	}

	@Test
	void testGroovyScriptInvokeMethodWithArgs() {
		Object result = groovyScriptExecutor.execute(testNormalGroovyScript, "testInvokeMethodWithArgs", 1);
		assertEquals(2, result);
	}

	@Test
	void testGroovyScriptInvokeMethodWithTwoArgs() {
		Object result = groovyScriptExecutor.execute(testNormalGroovyScript, "testInvokeMethodWithTwoArgs", 1, 2);
		assertEquals(3, result);
	}

	@Test
	void testGroovyScriptInstantiationException() throws IOException {
		final String script = readGroovyScriptAsString("TestInstantiationException.groovy");
		GroovyScriptParseException e = assertThrows(GroovyScriptParseException.class,
			() -> groovyScriptExecutor.execute(script, "test")
		);
		assertTrue(e.getMessage().contains("Failed to parse groovy class script"));
	}

	@Test
	void testEvaluateGroovyScriptSnippet() {
		final String scriptSnippet = "def test() { return 1 + 1 }";
		Object result = groovyScriptExecutor.evaluate(scriptSnippet, "test");
		assertEquals(2, result);
		// case: groovyObjectCache != null
		result = groovyScriptExecutor.evaluate(scriptSnippet, "test");
		assertEquals(2, result);
	}

	@Test
	void testGroovyObjectInvokeMethodException() {
		final String script = "def testAdd() { return 1 + 1 }";
		GroovyObjectInvokeMethodException e = assertThrows(GroovyObjectInvokeMethodException.class,
			() -> groovyScriptExecutor.evaluate(script, "test")
		);
		assertTrue(e.getMessage().contains("Failed to invoke groovy method"));
	}

	@Test
	void testGroovySecurityInterceptor() {
		// unsafe script case
		final String unsafeScript1 = "def testSystemClassInterceptor() { System.gc() }";
		GroovyObjectInvokeMethodException e = assertThrows(GroovyObjectInvokeMethodException.class,
			() -> groovyScriptExecutor.evaluate(unsafeScript1, "testSystemClassInterceptor")
		);
		assertTrue(e.getMessage().contains("not allowed in your groovy code"));

		// another unsafe script case
		final String unsafeScript2 = "def testRuntimeClassInterceptor() { Runtime.getRuntime().gc() }";
		e = assertThrows(GroovyObjectInvokeMethodException.class,
			() -> groovyScriptExecutor.evaluate(unsafeScript2, "testRuntimeClassInterceptor")
		);
		assertTrue(e.getMessage().contains("not allowed in your groovy code"));

		// safe script case
		final String safeScript = "def testSystemClassInterceptor() { return System.currentTimeMillis() }";
		Object result = groovyScriptExecutor.evaluate(safeScript, "testSystemClassInterceptor");
		assertTrue(result instanceof Long && Long.parseLong(result.toString()) > 0);
	}

}
