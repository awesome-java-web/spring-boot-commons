package com.github.awesome.springboot.commons;

import com.github.awesome.springboot.commons.scripting.groovy.GroovyScriptExecutor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * 这个类是一个自动配置类，主要用于配置一些 Spring Bean
 *
 * @author <a href="https://github.com/codeboyzhou">codeboyzhou</a>
 * @since 1.1.0
 */
@Configuration
public class SpringBeanAutoConfiguration {

    /**
     * 配置 GroovyScriptExecutor Bean
     *
     * @see GroovyScriptExecutor
     */
    @Bean
    public GroovyScriptExecutor groovyScriptExecutor() {
        return new GroovyScriptExecutor();
    }

}
