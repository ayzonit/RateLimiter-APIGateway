package com.project.api_gateway_service.config;

import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RouteConfig {

    @Bean
    public RouteLocator routeLocator(RouteLocatorBuilder routeLocatorBuilder) {
        return routeLocatorBuilder.routes()

                .route("like-service", r -> r
                        .path("/api/likes/**")
                        .uri("http://localhost:8081"))

                .route("comment-service", r -> r
                        .path("/api/comments/**")
                        .uri("http://localhost:8082"))

                .route("comment-service", r -> r
                        .path("/api/share/**")
                        .uri("http://localhost:8083"))

                .build();
    }
}
