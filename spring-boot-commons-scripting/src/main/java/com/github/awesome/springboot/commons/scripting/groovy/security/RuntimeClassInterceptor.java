package com.github.awesome.springboot.commons.scripting.groovy.security;

import org.kohsuke.groovy.sandbox.GroovyInterceptor;

/**
 * 自定义的 Groovy 拦截器，用于拦截对 {@link Runtime} 类的静态方法调用。
 * 该拦截器继承自 {@link GroovyInterceptor}，用于在 Groovy 代码执行时进行方法调用拦截。
 *
 * <p>该类的主要作用是在 Groovy 代码中，如果有尝试调用 {@link Runtime} 类的方法时，
 * 会抛出 {@link SecurityException} 异常，防止执行潜在的危险操作。</p>
 *
 * <p>此拦截器的设计目的是增加代码的安全性，防止 Groovy 脚本中不小心或恶意调用某些敏感方法，
 * 例如执行系统命令等。</p>
 *
 * @author <a href="https://github.com/codeboyzhou">codeboyzhou</a>
 * @since 1.0.5
 */
public class RuntimeClassInterceptor extends GroovyInterceptor {

    /**
     * 拦截 {@link Runtime} 类的静态方法调用。
     * 如果尝试调用 {@link Runtime} 的方法，则抛出 {@link SecurityException} 异常。
     *
     * @param invoker  调用的 Invoker 对象
     * @param receiver 被调用的类，通常是 {@link Runtime} 类
     * @param method   被调用的方法名称
     * @param args     方法参数列表
     * @return 拦截后的结果，如果不是 {@link Runtime} 的方法，则继续执行默认行为
     * @throws Throwable 如果是 {@link Runtime} 方法调用，则抛出 {@link SecurityException} 异常
     */
    @Override
    public Object onStaticCall(Invoker invoker, Class receiver, String method, Object... args) throws Throwable {
        if (receiver == Runtime.class) {
            throw new SecurityException("Runtime." + method + "() is not allowed in your groovy code");
        }
        return super.onStaticCall(invoker, receiver, method, args);
    }

}
