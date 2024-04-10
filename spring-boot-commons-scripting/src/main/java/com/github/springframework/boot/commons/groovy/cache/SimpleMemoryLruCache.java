package com.github.springframework.boot.commons.groovy.cache;

import java.util.LinkedHashMap;
import java.util.Map;

public class SimpleMemoryLruCache<K, V> {

    private long hitCount;

    private long missCount;

    private final Map<K, V> cache;

    public SimpleMemoryLruCache(final int maxSize) {
        this.cache = new LinkedHashMap<K, V>(1, 0.75f, true) {
            @Override
            protected boolean removeEldestEntry(Map.Entry<K, V> eldest) {
                return size() > maxSize;
            }
        };
    }

    public final V getIfPresent(final K key) {
        V value;
        synchronized (this) {
            value = this.cache.get(key);
            if (value == null) {
                this.missCount++;
                return null;
            } else {
                this.hitCount++;
                return value;
            }
        }
    }

    public final void put(final K key, final V value) {
        if (key == null || value == null) {
            throw new NullPointerException("key == null || value == null");
        }

        synchronized (this) {
            this.cache.put(key, value);
        }
    }

    public final String stats() {
        final String hitRate = String.format("%.2f%%", (double) this.hitCount / (this.hitCount + this.missCount) * 100);
        return String.format("%s{hitCount=%d, missCount=%d, hitRate=%s}", getClass().getSimpleName(), this.hitCount, this.missCount, hitRate);
    }

}
