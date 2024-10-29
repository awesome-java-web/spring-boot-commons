package com.github.springframework.boot.commons.groovy;

import com.github.springframework.boot.commons.groovy.security.RuntimeClassInterceptor;
import com.github.springframework.boot.commons.groovy.security.SystemClassInterceptor;
import org.codehaus.groovy.control.CompilerConfiguration;
import org.codehaus.groovy.control.customizers.CompilationCustomizer;
import org.kohsuke.groovy.sandbox.GroovyInterceptor;
import org.kohsuke.groovy.sandbox.SandboxTransformer;

public class GroovyScriptCompiler {

    private final CompilerConfiguration compilerConfiguration;

    public static GroovyScriptCompiler asDefault() {
        return new GroovyScriptCompiler();
    }

    public GroovyScriptCompiler() {
        this.compilerConfiguration = new CompilerConfiguration();
        this.addCompilationCustomizer(new SandboxTransformer());
        this.registerSecurityInterceptor(new SystemClassInterceptor());
        this.registerSecurityInterceptor(new RuntimeClassInterceptor());
    }

    public void addCompilationCustomizer(CompilationCustomizer customizer) {
        this.compilerConfiguration.addCompilationCustomizers(customizer);
    }

    public void registerSecurityInterceptor(GroovyInterceptor interceptor) {
        interceptor.register();
    }

    public CompilerConfiguration getCompilerConfiguration() {
        return compilerConfiguration;
    }

}
