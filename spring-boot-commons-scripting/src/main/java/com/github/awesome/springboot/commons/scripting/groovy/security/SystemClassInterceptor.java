package com.github.awesome.springboot.commons.scripting.groovy.security;

import org.kohsuke.groovy.sandbox.GroovyInterceptor;

import java.util.Arrays;
import java.util.List;

/**
 * 自定义的 Groovy 拦截器，用于拦截对 {@link System} 类的静态方法调用。
 * 该拦截器继承自 {@link GroovyInterceptor}，用于拦截 Groovy 代码中对 {@link System} 类的一些敏感方法调用。
 *
 * <p>该类的主要目的是防止在 Groovy 脚本中执行某些可能会影响系统安全的操作，例如调用 {@link System#gc()},
 * {@link System#exit(int)} 或 {@link System#runFinalization()} 等方法。</p>
 *
 * <p>如果 Groovy 脚本尝试调用这些方法，拦截器将抛出 {@link SecurityException} 异常，阻止这些操作的执行。</p>
 *
 * <p>该拦截器帮助提高脚本的安全性，避免执行潜在的危险操作。</p>
 *
 * @author <a href="https://github.com/codeboyzhou">codeboyzhou</a>
 * @since 1.0.5
 */
public class SystemClassInterceptor extends GroovyInterceptor {

    /**
     * 不允许调用的 {@link System} 类方法列表。
     * 包含以下方法：
     * - {@link System#gc()}
     * - {@link System#exit(int)}
     * - {@link System#runFinalization()}
     */
    private static final List<String> NOT_ALLOWED_METHODS = Arrays.asList("gc", "exit", "runFinalization");

    /**
     * 拦截 {@link System} 类的静态方法调用。
     * 如果调用的是 {@link System} 类中的被禁止的方法，则抛出 {@link SecurityException} 异常。
     *
     * @param invoker  调用的 {@link Invoker} 对象
     * @param receiver 被调用的类，通常是 {@link System} 类
     * @param method   被调用的方法名称
     * @param args     方法参数列表
     * @return 如果调用的是允许的方法，继续执行默认行为；否则抛出 {@link SecurityException} 异常
     * @throws Throwable 如果调用的是禁止的方法，则抛出 {@link SecurityException} 异常
     */
    @Override
    public Object onStaticCall(Invoker invoker, Class receiver, String method, Object... args) throws Throwable {
        if (receiver == System.class && NOT_ALLOWED_METHODS.contains(method)) {
            throw new SecurityException("System." + method + "() is not allowed in your groovy code");
        }
        return super.onStaticCall(invoker, receiver, method, args);
    }

}
