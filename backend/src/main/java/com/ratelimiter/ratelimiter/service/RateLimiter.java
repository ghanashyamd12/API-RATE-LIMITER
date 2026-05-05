package com.ratelimiter.ratelimiter.service;

import java.util.HashMap;
import java.util.Map;

public class RateLimiter {

    private final int maxTokens;
    private final int refillRate;

    private final Map<String, Integer> tokens;
    private final Map<String, Long> lastRefillTime;

    public RateLimiter(int maxTokens, int refillRate) {
        this.maxTokens = maxTokens;
        this.refillRate = refillRate;
        this.tokens = new HashMap<>();
        this.lastRefillTime = new HashMap<>();
    }

    public boolean allowRequest(String userId) {
        long currentTime = System.currentTimeMillis();

        // Step 1: Initialize if new user
        tokens.putIfAbsent(userId, maxTokens);
        lastRefillTime.putIfAbsent(userId, currentTime);

        // Step 2: Calculate time passed
        long lastTime = lastRefillTime.get(userId);
        long timePassed = (currentTime - lastTime) / 1000; // seconds

        // Step 3: Refill tokens
        int currentTokens = tokens.get(userId);
        int tokensToAdd = (int) (timePassed * refillRate);

        if (tokensToAdd > 0) {
            currentTokens = Math.min(maxTokens, currentTokens + tokensToAdd);
            lastRefillTime.put(userId, currentTime);
        }

        // Step 4: Check if request allowed
        if (currentTokens > 0) {
            tokens.put(userId, currentTokens - 1);
            return true;
        } else {
            return false;
        }
    }
}