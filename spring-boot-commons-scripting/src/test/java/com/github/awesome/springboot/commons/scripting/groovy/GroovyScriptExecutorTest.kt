package com.github.awesome.springboot.commons.scripting.groovy

import com.github.awesome.springboot.commons.scripting.groovy.exception.GroovyObjectInvokeMethodException
import com.github.awesome.springboot.commons.scripting.groovy.exception.GroovyScriptParseException
import com.github.awesome.springboot.commons.scripting.groovy.exception.InvalidGroovyScriptException
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import kotlin.test.assertEquals
import kotlin.test.assertTrue

class GroovyScriptExecutorTest {

    private val groovyScriptExecutor = GroovyScriptExecutor()

    @Test
    fun testExecute() {
        val e1 = assertThrows<InvalidGroovyScriptException>() {
            groovyScriptExecutor.execute(null, null)
        }
        assertEquals("Groovy script is null", e1.message)

        val e2 = assertThrows<InvalidGroovyScriptException>() {
            groovyScriptExecutor.execute("", null)
        }
        assertEquals("Groovy script is empty", e2.message)

        val groovyClassScript = """
            class GroovyClass {
                def execute() {
                    return "Hello, Groovy!"
                }

                def executeWithParam(String name) {
                    return "Hello, " + name
                }
            }
        """
        val result1 = groovyScriptExecutor.execute(groovyClassScript, "execute")
        assertEquals("Hello, Groovy!", result1)

        val result2 = groovyScriptExecutor.execute(groovyClassScript, "executeWithParam", "Groovy!")
        assertEquals("Hello, Groovy!", result2)

        val e3 = assertThrows<GroovyScriptParseException>() {
            groovyScriptExecutor.execute("interface GroovyInterface {}", "execute")
        }
        assertTrue { e3.message!!.startsWith("Failed to parse groovy class script") }

        val e4 = assertThrows<GroovyObjectInvokeMethodException>() {
            groovyScriptExecutor.execute(groovyClassScript, "noSuchMethod")
        }
        assertTrue { e4.message!!.startsWith("Failed to invoke groovy method") }
    }

    @Test
    fun testEvaluate() {
        val e1 = assertThrows<InvalidGroovyScriptException>() {
            groovyScriptExecutor.evaluate(null, null)
        }
        assertEquals("Groovy script is null", e1.message)

        val e2 = assertThrows<InvalidGroovyScriptException>() {
            groovyScriptExecutor.evaluate("", null)
        }
        assertEquals("Groovy script is empty", e2.message)

        val groovyScript = """
            def execute() {
                return "Hello, Groovy!"
            }

            def executeWithParam(String name) {
                return "Hello, " + name
            }
        """
        val result1 = groovyScriptExecutor.evaluate(groovyScript, "execute")
        assertEquals("Hello, Groovy!", result1)

        val result2 = groovyScriptExecutor.evaluate(groovyScript, "executeWithParam", "Groovy!")
        assertEquals("Hello, Groovy!", result2)

        val e3 = assertThrows<GroovyObjectInvokeMethodException>() {
            groovyScriptExecutor.evaluate(groovyScript, "noSuchMethod")
        }
        assertTrue { e3.message!!.startsWith("Failed to invoke groovy method") }
    }

    @Test
    fun testGroovyInterceptor() {
        val groovyScript = """
            def unsafeSystemMethod() {
                System.gc()
            }

            def safeSystemMethod() {
                System.currentTimeMillis()
            }

            def unsafeRuntimeClass() {
                Runtime.getRuntime().exec("ls")
            }
        """
        val e1 = assertThrows<GroovyObjectInvokeMethodException>() {
            groovyScriptExecutor.evaluate(groovyScript, "unsafeSystemMethod")
        }
        assertTrue { e1.message!!.contains("not allowed in your groovy code") }

        val result1 = groovyScriptExecutor.evaluate(groovyScript, "safeSystemMethod")
        assertTrue { result1 is Long && result1 > 0 }

        val e2 = assertThrows<GroovyObjectInvokeMethodException>() {
            groovyScriptExecutor.evaluate(groovyScript, "unsafeRuntimeClass")
        }
        assertTrue { e2.message!!.contains("not allowed in your groovy code") }
    }

}
