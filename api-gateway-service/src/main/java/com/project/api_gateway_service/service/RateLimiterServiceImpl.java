package com.project.api_gateway_service.service;

import com.project.api_gateway_service.model.RateLimitStatus;
import lombok.extern.java.Log;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.ReactiveStringRedisTemplate;
import org.springframework.data.redis.core.script.RedisScript;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import java.time.Instant;
import java.util.List;

@Service
public class RateLimiterServiceImpl implements RateLimiterService{


    @Autowired
    ReactiveStringRedisTemplate reactiveStringRedisTemplate;

    @Autowired
    RedisScript<List> tokenBucketScript;

    private static final long capacity = 100;
    private static final long refill_rate = 10;

    @Override
    public Mono<RateLimitStatus> checkRateLimit(String key) {
        String redisKey = "rate_limit"  + key;

        long now = Instant.now().getEpochSecond();
        long ttl = (capacity / refill_rate) * 2;

        return reactiveStringRedisTemplate.execute(
                tokenBucketScript,
                List.of(redisKey),
                String.valueOf(capacity),
                String.valueOf(refill_rate),
                String.valueOf(now),
                String.valueOf(ttl)
        )
        .single()
        .map(values -> {
            Long allowed = Long.parseLong(values.get(0).toString());
            Long remainingTokens = Long.parseLong(values.get(1).toString());
            Long resetTime = Long.parseLong(values.get(2).toString());

            return new RateLimitStatus(allowed == 1, remainingTokens, capacity, resetTime);
        });
    }
}
