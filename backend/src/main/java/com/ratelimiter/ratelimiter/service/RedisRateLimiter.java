package com.ratelimiter.ratelimiter.service;

import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;

@Component
public class RedisRateLimiter {

    private final StringRedisTemplate redisTemplate;

    public RedisRateLimiter(StringRedisTemplate redisTemplate) {
        this.redisTemplate = redisTemplate;
    }

    // 🔥 Token bucket logic
    public boolean allowRequest(String userId, int maxTokens) {

        int refillRate = 1; // tokens per second

        String tokenKey = "tokens:" + userId;
        String timeKey = "lastRefill:" + userId;

        long currentTime = System.currentTimeMillis();

        String tokensStr = redisTemplate.opsForValue().get(tokenKey);
        String lastTimeStr = redisTemplate.opsForValue().get(timeKey);

        int tokens;
        long lastTime;

        if (tokensStr == null || lastTimeStr == null) {
            tokens = maxTokens;
            lastTime = currentTime;
        } else {
            tokens = Integer.parseInt(tokensStr);
            lastTime = Long.parseLong(lastTimeStr);
        }

        long timePassed = (currentTime - lastTime) / 1000;
        int tokensToAdd = (int) (timePassed * refillRate);

        if (tokensToAdd > 0) {
            tokens = Math.min(maxTokens, tokens + tokensToAdd);
            lastTime = currentTime;
        }

        if (tokens > 0) {
            tokens--;

            redisTemplate.opsForValue().set(tokenKey, String.valueOf(tokens));
            redisTemplate.opsForValue().set(timeKey, String.valueOf(lastTime));

            return true;
        } else {
            redisTemplate.opsForValue().set(timeKey, String.valueOf(lastTime));
            return false;
        }
    }

    // ✅ ADD THIS METHOD INSIDE THE CLASS
    public int getRemainingTokens(String userId) {
        String tokenKey = "tokens:" + userId;
        String tokensStr = redisTemplate.opsForValue().get(tokenKey);

        return (tokensStr == null) ? 0 : Integer.parseInt(tokensStr);
    }
}