package com.github.springframework.boot.commons.util.spring;

import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.script.DefaultRedisScript;
import org.springframework.data.redis.core.script.RedisScript;

import java.util.Collections;
import java.util.concurrent.TimeUnit;

public final class RedisTemplates {

	public RedisTemplates() {
		throw new UnsupportedOperationException("Utility class should not be instantiated");
	}

	public static boolean setIfAbsent(StringRedisTemplate redisTemplate, String key, String value, long expiration, TimeUnit unit) {
		final String luaScript = "return redis.call('SET', KEYS[1], ARGV[1], 'NX', 'EX', ARGV[2]) and true or false";
		final String seconds = String.valueOf(unit.toSeconds(expiration));
		RedisScript<Boolean> redisScript = new DefaultRedisScript<>(luaScript, Boolean.class);
		Boolean result = redisTemplate.execute(redisScript, Collections.singletonList(key), value, seconds);
		return Boolean.TRUE.equals(result);
	}

}
