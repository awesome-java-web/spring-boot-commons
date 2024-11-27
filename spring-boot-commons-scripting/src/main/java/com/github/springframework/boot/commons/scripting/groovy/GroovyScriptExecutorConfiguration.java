package com.github.springframework.boot.commons.scripting.groovy;

import com.github.springframework.boot.commons.scripting.ScriptExecutorConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConditionalOnProperty(prefix = "groovy")
@ConfigurationProperties(prefix = "groovy")
public class GroovyScriptExecutorConfiguration extends ScriptExecutorConfiguration {

    private int groovyObjectCacheMaxSize = 10;

    public int getGroovyObjectCacheMaxSize() {
        return groovyObjectCacheMaxSize;
    }

    public void setGroovyObjectCacheMaxSize(int groovyObjectCacheMaxSize) {
        this.groovyObjectCacheMaxSize = groovyObjectCacheMaxSize;
    }

}
