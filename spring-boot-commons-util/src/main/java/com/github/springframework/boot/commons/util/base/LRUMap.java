package com.github.springframework.boot.commons.util.base;

import java.util.LinkedHashMap;
import java.util.Map;

public class LRUMap<K, V> extends LinkedHashMap<K, V> {

	private static final int DEFAULT_INITIAL_CAPACITY = 1 << 4;

	private static final float DEFAULT_LOAD_FACTOR = 0.75f;

	private final int maxCapacity;

	public LRUMap(int maxCapacity) {
		super(DEFAULT_INITIAL_CAPACITY, DEFAULT_LOAD_FACTOR, true);
		this.maxCapacity = maxCapacity;
	}

	@Override
	protected boolean removeEldestEntry(Map.Entry<K, V> eldest) {
		return size() > maxCapacity;
	}

}
