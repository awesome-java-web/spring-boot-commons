package com.github.springframework.boot.commons.groovy.cache;

import groovy.lang.GroovyObject;

public class LocalCacheManager implements LocalCache {

    private LocalCache useCache;

    public static LocalCacheManager newBuilder() {
        return new LocalCacheManager();
    }

    public LocalCacheManager use(LocalCache localCache) {
        this.useCache = localCache;
        return this;
    }

    public LocalCacheManager useDefaultCache() {
        SimpleMemoryLruCache<String, GroovyObject> defaultCache = new SimpleMemoryLruCache<>(5);
        this.useCache = new DefaultLocalCache(defaultCache);
        return this;
    }

    @Override
    public GroovyObject getIfPresent(String key) {
        return this.useCache.getIfPresent(key);
    }

    @Override
    public void put(String key, GroovyObject groovyObject) {
        this.useCache.put(key, groovyObject);
    }

    @Override
    public String stats() {
        return this.useCache.stats();
    }

}
