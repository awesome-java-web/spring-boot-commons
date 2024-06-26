package com.github.springframework.boot.commons.groovy.cache;

import com.google.common.cache.Cache;
import groovy.lang.GroovyObject;

public class GuavaLocalCache implements LocalCache {

    private final Cache<String, GroovyObject> cache;

    public GuavaLocalCache(Cache<String, GroovyObject> cache) {
        this.cache = cache;
    }

    @Override
    public GroovyObject getIfPresent(String key) {
        return this.cache.getIfPresent(key);
    }

    @Override
    public void put(String key, GroovyObject groovyObject) {
        this.cache.put(key, groovyObject);
    }

    @Override
    public String stats() {
        return String.format("[%s]%s", getClass().getSimpleName(), this.cache.stats());
    }

}
