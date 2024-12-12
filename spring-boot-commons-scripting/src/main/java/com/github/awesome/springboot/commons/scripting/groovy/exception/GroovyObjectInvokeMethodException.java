package com.github.awesome.springboot.commons.scripting.groovy.exception;

/**
 * 自定义异常类，用于表示在调用 Groovy 对象方法时发生的异常。
 * 该异常继承自 {@link RuntimeException}，用于在运行时抛出异常。
 * 主要用于在处理 Groovy 脚本或 Groovy 对象时，捕获并传递调用方法时出现的问题。
 *
 * <p>示例：在调用 Groovy 对象的方法时，如果发生异常，可以抛出此异常以提供详细的错误信息和根本原因。</p>
 *
 * <p>构造方法接受一条错误信息和一个导致异常的原因 {@link Throwable}，用于进一步的异常分析。</p>
 *
 * @since 1.0.5
 */
public class GroovyObjectInvokeMethodException extends RuntimeException {

    /**
     * 构造一个新的 {@code GroovyObjectInvokeMethodException} 异常。
     *
     * @param message 异常的详细错误信息，描述为什么会抛出该异常
     * @param cause   引起此异常的根本原因，通常是另一个异常
     */
    public GroovyObjectInvokeMethodException(String message, Throwable cause) {
        super(message, cause);
    }

}
