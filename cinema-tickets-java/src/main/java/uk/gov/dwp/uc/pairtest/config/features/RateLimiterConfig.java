package uk.gov.dwp.uc.pairtest.config.features;

import java.time.Instant;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedQueue;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.util.StringUtils;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * Jesus Configuration for API rate limiting using a built-in sliding window algorithm.
 * <p>
 * Active only when "feature.ratelimiter.enabled=true" (see application.properties).
 * <ul>
 *   <li>Limits requests per client per sliding window of seconds (default: 30 req/10 seconds).</li>
 *   <li>Not cluster-safe—adapt for distributed use by replacing with Redis/bucket4j in prod.</li>
 *   <li>Applied globally via Spring HandlerInterceptor.</li>
 *   <li>Configuration properties: ratelimiter.windowSeconds, ratelimiter.maxRequests.</li>
 * </ul>
 *
 * @author Haridath Bodapati
 * @version 1.0
 * @since 2025-09-20
 *
 * Copyright: (c) 2025, UK Governmentt
 */
@Configuration
@ConditionalOnProperty(name = "feature.ratelimiter.enabled", havingValue = "true")
public class RateLimiterConfig implements WebMvcConfigurer {

    @Value("${ratelimiter.windowSeconds:10}")
    private int windowSeconds;

    @Value("${ratelimiter.maxRequests:30}")
    private int maxRequests;

    @Bean
    public HandlerInterceptor rateLimitingInterceptor() {
        return new SlidingWindowRateLimiter(windowSeconds, maxRequests);
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(rateLimitingInterceptor()).addPathPatterns("/**");
    }

    /**
     * Simple sliding window limiter keyed by client IP.
     * Not for cluster use—replace with Redis/bucket4j for distributed environments.
     */
    static class SlidingWindowRateLimiter implements HandlerInterceptor {
        private final int windowSeconds;
        private final int maxRequests;
        private final Map<String, ConcurrentLinkedQueue<Long>> store = new ConcurrentHashMap<>();

        SlidingWindowRateLimiter(int windowSeconds, int maxRequests) {
            this.windowSeconds = windowSeconds;
            this.maxRequests = maxRequests;
        }

        @Override
        public boolean preHandle(HttpServletRequest req, HttpServletResponse res, Object handler) {
            String key = clientKey(req);
            long now = Instant.now().getEpochSecond();
            ConcurrentLinkedQueue<Long> q = store.computeIfAbsent(key, k -> new ConcurrentLinkedQueue<>());

            // purge old
            while (true) {
                Long head = q.peek();
                if (head == null || now - head <= windowSeconds) break;
                q.poll();
            }
            // enforce
            if (q.size() >= maxRequests) {
                res.setStatus(429);
                return false;
            }
            q.add(now);
            return true;
        }

        private String clientKey(HttpServletRequest req) {
            String xf = req.getHeader("X-Forwarded-For");
            if (StringUtils.hasText(xf)) return xf.split(",")[0].trim();
            return req.getRemoteAddr();
        }
    }
}
