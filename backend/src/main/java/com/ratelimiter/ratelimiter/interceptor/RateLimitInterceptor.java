package com.ratelimiter.ratelimiter.interceptor;

import com.ratelimiter.ratelimiter.config.RateLimitConfig;
import com.ratelimiter.ratelimiter.service.RateLimiterService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

@Component
public class RateLimitInterceptor implements HandlerInterceptor {

    private final RateLimiterService rateLimiterService;

    public RateLimitInterceptor(RateLimiterService rateLimiterService) {
        this.rateLimiterService = rateLimiterService;
    }

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {

        // ✅ Allow preflight (CORS)
        if ("OPTIONS".equalsIgnoreCase(request.getMethod())) {
            response.setStatus(200);
            return true;
        }

        String apiKey = request.getHeader("X-API-KEY");

        // ❌ Missing API key
        if (apiKey == null || apiKey.isEmpty()) {
            response.setStatus(400);
            response.getWriter().write("Missing API Key");
            return false;
        }

        // 🔥 (Optional but useful for logs/debugging)
        int limit = RateLimitConfig.getLimit(apiKey);

        // ✅ Rate limiting
        if (rateLimiterService.allowRequest(apiKey)) {
            return true;
        } else {
            response.setStatus(429);
            response.getWriter().write(
                    "Too Many Requests (Limit: " + limit + ")"
            );
            return false;
        }
    }
}