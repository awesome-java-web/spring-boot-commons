package com.github.awesome.springboot.commons.scripting.groovy.exception;

/**
 * 自定义异常类，用于表示无效的 Groovy 脚本异常。
 * 该异常继承自 {@link RuntimeException}，用于在运行时抛出异常。
 * 当 Groovy 脚本无效或无法执行时，可以抛出此异常。
 *
 * <p>此异常通常用于标识 Groovy 脚本存在错误或不符合预期的情况，例如脚本格式不正确、缺少必要的内容等。</p>
 *
 * <p>构造方法接受一条错误信息，用于详细描述为什么该 Groovy 脚本无效。</p>
 *
 * @since 1.0.5
 */
public class InvalidGroovyScriptException extends RuntimeException {

    /**
     * 构造一个新的 {@code InvalidGroovyScriptException} 异常。
     *
     * @param message 异常的详细错误信息，描述为什么脚本无效或无法执行
     */
    public InvalidGroovyScriptException(String message) {
        super(message);
    }

}
