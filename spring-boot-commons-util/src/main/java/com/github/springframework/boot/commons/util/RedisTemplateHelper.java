package com.github.springframework.boot.commons.util;

import org.springframework.data.redis.connection.RedisScriptingCommands;
import org.springframework.data.redis.connection.ReturnType;
import org.springframework.data.redis.core.RedisCallback;
import org.springframework.data.redis.core.StringRedisTemplate;

import java.util.concurrent.TimeUnit;

public final class RedisTemplateHelper {

	RedisTemplateHelper() {
		throw new UnsupportedOperationException("Utility class should not be instantiated");
	}

	public static boolean setIfAbsent(StringRedisTemplate redisTemplate, String key, String value, long expiration, TimeUnit unit) {
		final String luaScript = "return redis.call('SET', KEYS[1], ARGV[1], 'NX', 'EX', ARGV[2]) and true or false";
		final String seconds = String.valueOf(unit.toSeconds(expiration));
		final Boolean result = redisTemplate.execute(
			(RedisCallback<Boolean>) connection -> {
				RedisScriptingCommands redisScriptingCommands = connection.scriptingCommands();
				return redisScriptingCommands.eval(
					luaScript.getBytes(), ReturnType.BOOLEAN, 1, key.getBytes(), value.getBytes(), seconds.getBytes()
				);
			}
		);
		return Boolean.TRUE.equals(result);
	}

}
