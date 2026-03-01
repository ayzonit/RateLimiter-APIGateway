package com.project.api_gateway_service.service;

import com.project.api_gateway_service.model.RateLimitStatus;
import reactor.core.publisher.Mono;

public interface RateLimiterService {

    Mono<RateLimitStatus> checkRateLimit(String key);
}
