package com.github.awesome.springboot.commons.lang

import org.junit.jupiter.api.Test
import java.util.concurrent.Executors

class ExceptionAwareRunnableTest {

    @Test
    fun testRun() {
        val pool = Executors.newSingleThreadExecutor()
        pool.execute(ExceptionAwareRunnable("ExceptionAwareRunnableTest") {
            throw RuntimeException("test")
        })
        pool.execute(ExceptionAwareRunnable("ExceptionAwareRunnableTest") {
            println("test")
        })
    }

}
