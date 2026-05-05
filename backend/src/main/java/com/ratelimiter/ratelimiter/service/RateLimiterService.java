package com.ratelimiter.ratelimiter.service;

import com.ratelimiter.ratelimiter.config.RateLimitConfig;
import org.springframework.stereotype.Service;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Service
public class RateLimiterService {

    private static final Logger logger = LoggerFactory.getLogger(RateLimiterService.class);

    private final RedisRateLimiter redisRateLimiter;

    public RateLimiterService(RedisRateLimiter redisRateLimiter) {
        this.redisRateLimiter = redisRateLimiter;
    }

    public boolean allowRequest(String userId) {

        int limit = RateLimitConfig.getLimit(userId);

        boolean allowed = redisRateLimiter.allowRequest(userId, limit);

        if (allowed) {
            logger.info("Request allowed for user: {} (Plan: {}, Limit: {})",
                    userId,
                    RateLimitConfig.getPlan(userId),
                    limit);
        } else {
            logger.warn("Request blocked for user: {} (Plan: {}, Limit: {})",
                    userId,
                    RateLimitConfig.getPlan(userId),
                    limit);
        }

        return allowed;
    }

    // ✅ THIS MUST BE INSIDE THE CLASS (before final })
    public int getRemainingTokens(String userId) {
        return redisRateLimiter.getRemainingTokens(userId);
    }
}