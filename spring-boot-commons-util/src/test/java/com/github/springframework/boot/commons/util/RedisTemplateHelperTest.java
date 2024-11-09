package com.github.springframework.boot.commons.util;

import org.junit.jupiter.api.Test;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.script.RedisScript;

import java.util.concurrent.TimeUnit;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.Mockito.*;

class RedisTemplateHelperTest {

	private final StringRedisTemplate mockedStringRedisTemplate = mock(StringRedisTemplate.class);

	@Test
	void testNewInstance() {
		UnsupportedOperationException e = assertThrows(UnsupportedOperationException.class, RedisTemplateHelper::new);
		assertEquals("Utility class should not be instantiated", e.getMessage());
	}

	@Test
	void testSetIfAbsent() {
		when(mockedStringRedisTemplate.execute(any(RedisScript.class), anyList(), any())).thenReturn(true);
		assertTrue(RedisTemplateHelper.setIfAbsent(mockedStringRedisTemplate, "key", "value", 1, TimeUnit.SECONDS));
		verify(mockedStringRedisTemplate, times(1)).execute(any(RedisScript.class), anyList(), any());
	}

}
