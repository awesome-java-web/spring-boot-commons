package com.github.awesome.springboot.commons.scripting.groovy;

import com.github.awesome.springboot.commons.scripting.groovy.security.RuntimeClassInterceptor;
import com.github.awesome.springboot.commons.scripting.groovy.security.SystemClassInterceptor;
import org.codehaus.groovy.control.CompilerConfiguration;
import org.codehaus.groovy.control.customizers.CompilationCustomizer;
import org.kohsuke.groovy.sandbox.GroovyInterceptor;
import org.kohsuke.groovy.sandbox.SandboxTransformer;

/**
 * Groovy 脚本编译器类，用于配置和编译 Groovy 脚本，同时提供安全拦截机制。
 *
 * <p>该类封装了 {@link CompilerConfiguration} 对象，允许通过自定义编译器配置来定制 Groovy 脚本的编译行为。</p>
 * <p>在构造函数中，默认添加了一些安全拦截器，如 {@link SandboxTransformer} 和 {@link SystemClassInterceptor}，以防止脚本执行潜在的危险操作。</p>
 * <p>该类还提供了方法来注册自定义的拦截器和编译器定制器，以进一步增强安全性和功能性。</p>
 *
 * @author <a href="https://github.com/codeboyzhou">codeboyzhou</a>
 * @since 1.1.0
 */
public class GroovyScriptCompiler {

    /**
     * Groovy 编译器配置对象，包含编译相关的设置。
     */
    private final CompilerConfiguration compilerConfiguration;

    /**
     * 构造函数，初始化 Groovy 脚本编译器配置，并注册默认的安全拦截器。
     *
     * <p>默认注册以下拦截器：</p>
     * <ul>
     *   <li>{@link SandboxTransformer}：用于沙盒化 Groovy 脚本，限制其执行范围。</li>
     *   <li>{@link SystemClassInterceptor}：用于拦截对 {@link System} 类的敏感方法调用，防止潜在的系统安全风险。</li>
     *   <li>{@link RuntimeClassInterceptor}：用于拦截对 {@link Runtime} 类的敏感方法调用，防止可能的恶意行为。</li>
     * </ul>
     */
    public GroovyScriptCompiler() {
        compilerConfiguration = new CompilerConfiguration();
        addCompilationCustomizer(new SandboxTransformer());
        registerSecurityInterceptor(new SystemClassInterceptor());
        registerSecurityInterceptor(new RuntimeClassInterceptor());
    }

    /**
     * 添加自定义的编译器定制器。
     *
     * <p>通过此方法可以向编译器配置中添加额外的定制器，用于修改脚本编译行为。</p>
     *
     * @param customizer 需要添加的编译器定制器
     */
    public void addCompilationCustomizer(CompilationCustomizer customizer) {
        compilerConfiguration.addCompilationCustomizers(customizer);
    }

    /**
     * 注册安全拦截器。
     *
     * <p>通过此方法可以注册自定义的安全拦截器，用于在脚本执行过程中拦截特定的操作，增强安全性。</p>
     *
     * @param interceptor 需要注册的安全拦截器
     */
    public void registerSecurityInterceptor(GroovyInterceptor interceptor) {
        interceptor.register();
    }

    /**
     * 获取当前的编译器配置。
     *
     * @return 当前的 {@link CompilerConfiguration} 对象
     */
    public CompilerConfiguration getCompilerConfiguration() {
        return compilerConfiguration;
    }

}
