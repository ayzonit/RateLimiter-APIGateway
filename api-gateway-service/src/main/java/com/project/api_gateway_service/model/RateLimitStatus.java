package com.project.api_gateway_service.model;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class RateLimitStatus {

    private final boolean allowed;
    private final long remainingTokens;
    private final long limit;
    private final long resetTime;

}
