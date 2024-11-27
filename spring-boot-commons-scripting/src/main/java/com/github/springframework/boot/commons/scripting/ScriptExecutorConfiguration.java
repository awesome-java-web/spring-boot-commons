package com.github.springframework.boot.commons.scripting;

import com.github.springframework.boot.commons.scripting.groovy.GroovyScriptExecutorConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConditionalOnProperty(prefix = "spring-boot-commons.script")
@ConfigurationProperties(prefix = "spring-boot-commons.script")
public class ScriptExecutorConfiguration {
    private GroovyScriptExecutorConfiguration groovy;
}
