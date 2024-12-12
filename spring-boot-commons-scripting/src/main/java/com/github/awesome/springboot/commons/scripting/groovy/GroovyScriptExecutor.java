package com.github.awesome.springboot.commons.scripting.groovy;

import com.github.awesome.springboot.commons.scripting.groovy.exception.GroovyObjectInvokeMethodException;
import com.github.awesome.springboot.commons.scripting.groovy.exception.GroovyScriptParseException;
import com.github.awesome.springboot.commons.scripting.groovy.exception.InvalidGroovyScriptException;
import groovy.lang.GroovyClassLoader;
import groovy.lang.GroovyObject;
import groovy.lang.GroovyShell;
import org.apache.commons.codec.digest.DigestUtils;
import org.apache.groovy.util.concurrent.ConcurrentReferenceHashMap;
import org.codehaus.groovy.control.CompilerConfiguration;

import java.io.IOException;
import java.util.Map;

/**
 * Groovy 脚本执行器类，用于执行 Groovy 脚本并缓存解析后的对象。
 *
 * <p>该类提供了两种执行方式：</p>
 * <ul>
 *   <li>通过类脚本执行，执行指定的 Groovy 类脚本并调用指定的函数。</li>
 *   <li>通过脚本片段执行，执行指定的 Groovy 脚本片段并调用指定的函数。</li>
 * </ul>
 *
 * <p>该类内部会对解析后的 Groovy 脚本对象进行缓存，避免重复解析相同的脚本，提高执行效率，同时减少内存压力。</p>
 *
 * @since 1.0.5
 */
public class GroovyScriptExecutor {

    /**
     * Groovy 脚本对象的缓存，使用 MD5 值作为脚本缓存的 key。
     * <p>使用 {@link ConcurrentReferenceHashMap} 作为缓存，支持并发读写，并且在内存不足时允许垃圾回收机制优先回收缓存内的对象。</p>
     */
    private final Map<String, GroovyObject> cachedGroovyObject = new ConcurrentReferenceHashMap<>();

    /**
     * Groovy 脚本编译器，用于定制和配置脚本的编译方式。
     */
    private final GroovyScriptCompiler groovyScriptCompiler = new GroovyScriptCompiler();

    /**
     * 执行指定的 Groovy 类脚本中的方法。
     *
     * <p>此方法首先检查缓存中是否已解析该脚本，如果缓存中没有，则会解析并缓存该脚本。然后调用指定的函数，并传入参数。</p>
     *
     * @param classScript 要执行的 Groovy 类脚本
     * @param function    要调用的方法名
     * @param parameters  调用方法时的参数
     * @return 方法执行的返回结果
     * @throws GroovyScriptParseException        如果脚本解析失败
     * @throws GroovyObjectInvokeMethodException 如果方法调用失败
     */
    public Object execute(final String classScript, final String function, final Object... parameters) {
        GroovyObject groovyObjectCache = getGroovyObjectCacheFromScript(classScript);
        if (groovyObjectCache == null) {
            final String trimmedScript = classScript.trim();
            final String scriptCacheKey = DigestUtils.md5Hex(trimmedScript);
            groovyObjectCache = parseClassScript(trimmedScript);
            cachedGroovyObject.put(scriptCacheKey, groovyObjectCache);
        }
        return invokeMethod(groovyObjectCache, function, parameters);
    }

    /**
     * 执行指定的 Groovy 脚本片段中的方法。
     *
     * <p>此方法首先检查缓存中是否已解析该脚本片段，如果缓存中没有，则会解析并缓存该脚本片段。然后调用指定的函数，并传入参数。</p>
     *
     * @param scriptText 要执行的 Groovy 脚本片段
     * @param function   要调用的方法名
     * @param parameters 调用方法时的参数
     * @return 方法执行的返回结果
     * @throws GroovyScriptParseException        如果脚本解析失败
     * @throws GroovyObjectInvokeMethodException 如果方法调用失败
     */
    public Object evaluate(final String scriptText, final String function, final Object... parameters) {
        GroovyObject groovyObjectCache = getGroovyObjectCacheFromScript(scriptText);
        if (groovyObjectCache == null) {
            final String trimmedScript = scriptText.trim();
            final String scriptCacheKey = DigestUtils.md5Hex(trimmedScript);
            groovyObjectCache = parseScriptSnippet(trimmedScript);
            cachedGroovyObject.put(scriptCacheKey, groovyObjectCache);
        }
        return invokeMethod(groovyObjectCache, function, parameters);
    }

    /**
     * 从缓存中获取解析后的 Groovy 脚本对象。
     *
     * <p>如果缓存中没有该脚本对象，则返回 null。</p>
     *
     * @param scriptText 要查找的 Groovy 脚本
     * @return 已解析的 Groovy 脚本对象，或 null 如果未缓存该脚本
     * @throws InvalidGroovyScriptException 如果脚本为 null 或空
     */
    private GroovyObject getGroovyObjectCacheFromScript(final String scriptText) {
        if (scriptText == null) {
            throw new InvalidGroovyScriptException("Groovy script is null");
        }

        final String trimmedScript = scriptText.trim();
        if (trimmedScript.isEmpty()) {
            throw new InvalidGroovyScriptException("Groovy script is empty");
        }

        final String scriptCacheKey = DigestUtils.md5Hex(trimmedScript);
        return cachedGroovyObject.get(scriptCacheKey);
    }

    /**
     * 解析并生成 Groovy 类对象。
     *
     * <p>将 Groovy 类脚本通过 GroovyClassLoader 进行解析并返回 GroovyObject。</p>
     *
     * @param classScript Groovy 类脚本
     * @return 解析后的 Groovy 类对象
     * @throws GroovyScriptParseException 如果解析失败
     */
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

    /**
     * 解析并生成 Groovy 脚本片段对象。
     *
     * <p>将 Groovy 脚本片段通过 GroovyShell 进行解析并返回 GroovyObject。</p>
     *
     * @param scriptText Groovy 脚本片段
     * @return 解析后的 Groovy 脚本对象
     */
    private GroovyObject parseScriptSnippet(final String scriptText) {
        CompilerConfiguration configuration = groovyScriptCompiler.getCompilerConfiguration();
        GroovyShell groovyShell = new GroovyShell(configuration);
        return groovyShell.parse(scriptText);
    }

    /**
     * 调用指定 Groovy 对象的方法。
     *
     * <p>通过反射调用 Groovy 对象的指定方法，并传入参数。</p>
     *
     * @param groovyObject 要调用方法的 Groovy 对象
     * @param function     要调用的方法名
     * @param parameters   调用方法时的参数
     * @return 方法执行的返回结果
     * @throws GroovyObjectInvokeMethodException 如果方法调用失败
     */
    private Object invokeMethod(GroovyObject groovyObject, String function, Object... parameters) {
        try {
            return groovyObject.invokeMethod(function, parameters);
        } catch (Exception e) {
            final String errorMessage = String.format("Failed to invoke groovy method '%s', nested exception is %s", function, e);
            throw new GroovyObjectInvokeMethodException(errorMessage, e);
        }
    }

}
