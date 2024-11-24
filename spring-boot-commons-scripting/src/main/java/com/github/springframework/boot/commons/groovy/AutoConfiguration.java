package com.github.springframework.boot.commons.groovy;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class AutoConfiguration {

    @Bean
    public GroovyScriptExecutor groovyScriptExecutor() {
        return new GroovyScriptExecutor();
    }

}
