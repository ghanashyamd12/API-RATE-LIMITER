package com.ratelimiter.ratelimiter.config;

import java.util.Map;

public class RateLimitConfig {

    public static final Map<String, Integer> USER_PLANS = Map.of(
            "user1", 5,     // FREE
            "user2", 20     // PREMIUM
    );

    public static int getLimit(String apiKey) {
        return USER_PLANS.getOrDefault(apiKey, 5);
    }

    public static String getPlan(String apiKey) {
        return USER_PLANS.getOrDefault(apiKey, 5) > 5 ? "PREMIUM" : "FREE";
    }
}