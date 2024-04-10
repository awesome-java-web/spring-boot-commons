package com.github.springframework.boot.commons.groovy.cache;

import groovy.lang.GroovyObject;

public interface LocalCache {

    GroovyObject getIfPresent(final String key);

    void put(final String key, GroovyObject groovyObject);

    String stats();

}
