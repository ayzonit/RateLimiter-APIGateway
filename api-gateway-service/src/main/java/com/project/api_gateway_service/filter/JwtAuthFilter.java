package com.project.api_gateway_service.filter;

import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.Ordered;
import org.springframework.security.core.context.ReactiveSecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

@Component
public class JwtAuthFilter implements GlobalFilter, Ordered {

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        return ReactiveSecurityContextHolder.getContext()
                .map(securityContext -> securityContext.getAuthentication())
                .defaultIfEmpty(null)
                .flatMap(authentication -> {
                    if(authentication != null && authentication.isAuthenticated()) {
                        String userId = authentication.getName();
                        exchange.getAttributes().put("userId", userId);
                    }
                    return chain.filter(exchange);
                });
    }
    @Override
    public int getOrder() {
        return -2;
    }
}
