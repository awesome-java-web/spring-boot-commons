package com.github.springframework.boot.commons.scripting.groovy;

import com.github.springframework.boot.commons.scripting.groovy.security.RuntimeClassInterceptor;
import com.github.springframework.boot.commons.scripting.groovy.security.SystemClassInterceptor;
import org.codehaus.groovy.control.CompilerConfiguration;
import org.codehaus.groovy.control.customizers.CompilationCustomizer;
import org.kohsuke.groovy.sandbox.GroovyInterceptor;
import org.kohsuke.groovy.sandbox.SandboxTransformer;

public class GroovyScriptCompiler {

	private final CompilerConfiguration compilerConfiguration;

	public static GroovyScriptCompiler defaultCompiler() {
		return new GroovyScriptCompiler();
	}

	public GroovyScriptCompiler() {
		compilerConfiguration = new CompilerConfiguration();
		addCompilationCustomizer(new SandboxTransformer());
		registerSecurityInterceptor(new SystemClassInterceptor());
		registerSecurityInterceptor(new RuntimeClassInterceptor());
	}

	public void addCompilationCustomizer(CompilationCustomizer customizer) {
		compilerConfiguration.addCompilationCustomizers(customizer);
	}

	public void registerSecurityInterceptor(GroovyInterceptor interceptor) {
		interceptor.register();
	}

	public CompilerConfiguration getCompilerConfiguration() {
		return compilerConfiguration;
	}

}
