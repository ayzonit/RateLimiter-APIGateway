package com.project.api_gateway_service.filter;

import com.project.api_gateway_service.model.RateLimitStatus;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.Ordered;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

@Component
public class RateLimitHeaderFilter implements GlobalFilter, Ordered {

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {

        return chain.filter(exchange).then(Mono.fromRunnable(() -> {

            RateLimitStatus rateLimitStatus = exchange.getAttribute("RateLimitStatus");

            if (rateLimitStatus != null) {

                exchange.getResponse().getHeaders().add(
                        "Request Limit:",
                        String.valueOf(rateLimitStatus.getLimit())
                );

                exchange.getResponse().getHeaders().add(
                        "Remaining tokens:",
                        String.valueOf(rateLimitStatus.getRemainingTokens())
                );

                exchange.getResponse().getHeaders().add(
                        "Reset:",
                        String.valueOf(rateLimitStatus.getResetTime())
                );
            }
        }));
    }

    @Override
    public int getOrder() {
        return 0;
    }
}
