package com.ratelimiter.ratelimiter.controller;

import com.ratelimiter.ratelimiter.config.RateLimitConfig;
import com.ratelimiter.ratelimiter.service.RateLimiterService;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
public class HelloController {

    private final RateLimiterService rateLimiterService;

    public HelloController(RateLimiterService rateLimiterService) {
        this.rateLimiterService = rateLimiterService;
    }

    @GetMapping("/hello")
    public String sayHello() {
        return "Hello, Rate Limiter!";
    }

    @GetMapping("/api/status")
    public Map<String, String> getStatus() {
        return Map.of("status", "running");
    }

    @GetMapping("/api/user")
    public Map<String, String> getUser() {
        Map<String, String> response = new java.util.LinkedHashMap<>();
        response.put("name", "Ghanashyam");
        response.put("role", "Backend Developer");
        return response;
    }

    @GetMapping("/")
    public Map<String, String> home() {
        return Map.of("status", "API Rate Limiter Running");
    }

    // 🔥 FINAL IMPROVED ENDPOINT (used by frontend)
    @GetMapping("/api/test")
    public Map<String, Object> test(@RequestHeader("X-API-KEY") String apiKey) {

        int limit = RateLimitConfig.getLimit(apiKey);
        String plan = RateLimitConfig.getPlan(apiKey);

        // ⚠️ This method must exist in your service
        int remaining = rateLimiterService.getRemainingTokens(apiKey);

        Map<String, Object> response = new HashMap<>();
        response.put("message", "API Working");
        response.put("plan", plan);
        response.put("limit", limit);
        response.put("requestsLeft", remaining);

        return response;
    }
}