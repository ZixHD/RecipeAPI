package com.example.MobileAppBackend.config;

import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import java.time.Duration;

@Service
@RequiredArgsConstructor
public class RedisRateLimiter {

    private final StringRedisTemplate redisTemplate;

    private static final int LIMIT = 60;
    private static final Duration WINDOW = Duration.ofMinutes(1);

    public boolean allowRequest(String apiKey) {
        String key = "rate:" + apiKey;

        Long count = redisTemplate.opsForValue().increment(key);
        System.out.println("Hit redis: " + key + ": " + count);

        if (count != null && count == 1) {
            redisTemplate.expire(key, WINDOW);
        }

        return count != null && count <= LIMIT;
    }
}
