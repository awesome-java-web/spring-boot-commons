package com.github.springframework.boot.commons.scripting.groovy;

import com.github.springframework.boot.commons.scripting.groovy.exception.GroovyObjectInvokeMethodException;
import com.github.springframework.boot.commons.scripting.groovy.exception.GroovyScriptParseException;
import com.github.springframework.boot.commons.scripting.groovy.exception.InvalidGroovyScriptException;
import com.github.springframework.boot.commons.util.crypto.MessageDigests;
import groovy.lang.GroovyClassLoader;
import groovy.lang.GroovyObject;
import groovy.lang.GroovyShell;
import org.codehaus.groovy.control.CompilerConfiguration;
import org.springframework.util.ConcurrentReferenceHashMap;

import java.io.IOException;
import java.util.Map;

public class GroovyScriptExecutor {

    private final GroovyScriptCompiler groovyScriptCompiler = GroovyScriptCompiler.defaultCompiler();

    private final Map<String, GroovyObject> cachedGroovyObject = new ConcurrentReferenceHashMap<>();

    public Object execute(final String classScript, final String function, final Object... parameters) {
        // Parse the script and put it into cache instantly if it is not in cache
        GroovyObject groovyObjectCache = getGroovyObjectCacheFromScript(classScript);
        if (groovyObjectCache == null) {
            final String trimmedScript = classScript.trim();
            final String scriptCacheKey = MessageDigests.md5Hex(trimmedScript);
            groovyObjectCache = parseClassScript(trimmedScript);
            cachedGroovyObject.put(scriptCacheKey, groovyObjectCache);
        }

        // Script is parsed successfully
        return invokeMethod(groovyObjectCache, function, parameters);
    }

    public Object evaluate(final String scriptText, final String function, final Object... parameters) {
        // Parse the script and put it into cache instantly if it is not in cache
        GroovyObject groovyObjectCache = getGroovyObjectCacheFromScript(scriptText);
        if (groovyObjectCache == null) {
            final String trimmedScript = scriptText.trim();
            final String scriptCacheKey = MessageDigests.md5Hex(trimmedScript);
            groovyObjectCache = parseScriptSnippet(trimmedScript);
            cachedGroovyObject.put(scriptCacheKey, groovyObjectCache);
        }

        // Script is parsed successfully
        return invokeMethod(groovyObjectCache, function, parameters);
    }

    private GroovyObject getGroovyObjectCacheFromScript(final String scriptText) {
        if (scriptText == null) {
            throw new InvalidGroovyScriptException("Groovy script is null");
        }

        final String trimmedScript = scriptText.trim();
        if (trimmedScript.isEmpty()) {
            throw new InvalidGroovyScriptException("Groovy script is empty");
        }

        final String scriptCacheKey = MessageDigests.md5Hex(trimmedScript);
        return cachedGroovyObject.get(scriptCacheKey);
    }

    private GroovyObject parseClassScript(final String classScript) {
        ClassLoader currentClassLoader = getClass().getClassLoader();
        CompilerConfiguration configuration = groovyScriptCompiler.getCompilerConfiguration();
        try (GroovyClassLoader groovyClassLoader = new GroovyClassLoader(currentClassLoader, configuration)) {
            Class<?> scriptClass = groovyClassLoader.parseClass(classScript);
            return (GroovyObject) scriptClass.newInstance();
        } catch (IOException | InstantiationException | IllegalAccessException e) {
            final String errorMessage = String.format("Failed to parse groovy class script, nested exception is %s", e);
            throw new GroovyScriptParseException(errorMessage, e);
        }
    }

    private GroovyObject parseScriptSnippet(final String scriptText) {
        CompilerConfiguration configuration = groovyScriptCompiler.getCompilerConfiguration();
        GroovyShell groovyShell = new GroovyShell(configuration);
        return groovyShell.parse(scriptText);
    }

    private Object invokeMethod(GroovyObject groovyObject, String function, Object... parameters) {
        try {
            return groovyObject.invokeMethod(function, parameters);
        } catch (Exception e) {
            final String errorMessage = String.format("Failed to invoke groovy method '%s', nested exception is %s", function, e);
            throw new GroovyObjectInvokeMethodException(errorMessage, e);
        }
    }

}
