package com.project.api_gateway_service.filter;

import com.project.api_gateway_service.service.RateLimiterService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.Ordered;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

@Component
public class TokenBucketRateLimiterFilter implements GlobalFilter, Ordered {

    @Autowired
    RateLimiterService rateLimiterService;

    private String resolveKey(ServerWebExchange exchange) {
        String userId = exchange.getAttribute("userId");
        if (userId != null) {
            return "user:" + userId;
        }
        return "ip:" + exchange.getRequest().getRemoteAddress().getAddress().getHostAddress();
    }

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        String key = resolveKey(exchange);
        return rateLimiterService.checkRateLimit(key)
                .flatMap(status -> {
                     if (!status.isAllowed()) {
                         exchange.getResponse()
                                 .setStatusCode(HttpStatus.TOO_MANY_REQUESTS);
                         return exchange.getResponse().setComplete();
                     }
                     exchange.getAttributes().put("RateLimitStatus", status);
                     return chain.filter(exchange);
                });
    }

    @Override
    public int getOrder() {
        return -1;
    }
}
