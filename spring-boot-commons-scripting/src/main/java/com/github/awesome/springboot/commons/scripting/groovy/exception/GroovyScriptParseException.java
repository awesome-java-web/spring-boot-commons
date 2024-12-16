package com.github.awesome.springboot.commons.scripting.groovy.exception;

/**
 * 自定义异常类，用于表示在解析 Groovy 脚本时发生的异常。
 * 该异常继承自 {@link RuntimeException}，用于在运行时抛出异常。
 * 通常在解析 Groovy 脚本的过程中，如果出现语法错误或其他解析错误时，可以抛出此异常。
 *
 * <p>该异常提供了详细的错误信息以及导致解析失败的根本原因，帮助开发者定位问题。</p>
 *
 * <p>构造方法接受一条错误信息和一个导致异常的原因 {@link Throwable}，用于进一步的异常分析。</p>
 *
 * @author <a href="https://github.com/codeboyzhou">codeboyzhou</a>
 * @since 1.0.5
 */
public class GroovyScriptParseException extends RuntimeException {

    /**
     * 构造一个新的 {@code GroovyScriptParseException} 异常。
     *
     * @param message 异常的详细错误信息，描述解析脚本时发生的错误
     * @param cause   引起此异常的根本原因，通常是另一个异常，例如语法错误或脚本解析错误
     */
    public GroovyScriptParseException(String message, Throwable cause) {
        super(message, cause);
    }

}
