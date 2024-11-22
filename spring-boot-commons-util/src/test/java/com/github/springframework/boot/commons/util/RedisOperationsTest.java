package com.github.springframework.boot.commons.util;

import com.github.springframework.boot.commons.util.network.RedisOperations;
import org.junit.jupiter.api.Test;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.script.RedisScript;

import java.util.concurrent.TimeUnit;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.Mockito.*;

class RedisOperationsTest {

    private final StringRedisTemplate mockStringRedisTemplate = mock(StringRedisTemplate.class);

    @Test
    void testNewInstance() {
        UnsupportedOperationException e = assertThrows(UnsupportedOperationException.class, RedisOperations::new);
        assertEquals("Utility class should not be instantiated", e.getMessage());
    }

    @Test
    void testSetIfAbsent() {
        when(mockStringRedisTemplate.execute(any(RedisScript.class), anyList(), any())).thenReturn(true);
        assertTrue(RedisOperations.setIfAbsent("key", "value", 1, TimeUnit.SECONDS));
        verify(mockStringRedisTemplate, times(1)).execute(any(RedisScript.class), anyList(), any());
    }

}
