package com.project.api_gateway_service.service;

import com.project.api_gateway_service.model.RateLimitStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.ReactiveStringRedisTemplate;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import java.time.Instant;
import java.util.Map;

@Service
public class RateLimiterServiceImpl implements RateLimiterService{

    @Autowired
    ReactiveStringRedisTemplate reactiveStringRedisTemplate;

    private static final long capacity = 100;
    private static final long refill_rate = 10;

    @Override
    public Mono<RateLimitStatus> checkRateLimit(String key) {
        String redisKey = "rate-limit" + key;
        long now = Instant.now().getEpochSecond();

        return reactiveStringRedisTemplate.opsForHash().entries(redisKey)

                .collectMap(e ->
                        e.getKey().toString(), e -> e.getValue().toString())
                .flatMap(data -> {

                    long tokens = data.containsKey("tokens") ? Long.parseLong(data.get("tokens")) : capacity;
                    long lastRefill = data.containsKey("timestamp") ? Long.parseLong(data.get("timestamp")) : now;

                    long elapsed = now - lastRefill;
                    long refill = elapsed * refill_rate;

                    tokens = Math.min(capacity, tokens + refill);

                    boolean allowed = tokens > 0;
                    if (allowed) tokens--;

                    long resetTime = now + (capacity-tokens) / refill_rate;

                    Map<String, String> updated = Map.of("tokens", String.valueOf(tokens),
                            "timestamp", String.valueOf(now));

                    return reactiveStringRedisTemplate.opsForHash()
                            .putAll(redisKey, updated)
                            .thenReturn(new RateLimitStatus(allowed, tokens, capacity, resetTime));
                });
    }
}
