package org.example.config;

import org.example.converter.JwtAuthConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.web.server.SecurityWebFilterChain;

@Configuration
@EnableWebFluxSecurity
public class ResourceServerSecurityConfig {

    @Bean
    public SecurityWebFilterChain securityWebFilterChain(ServerHttpSecurity httpSecurity) {
        httpSecurity
                .csrf(ServerHttpSecurity.CsrfSpec::disable)
                .authorizeExchange(exchange -> exchange
                        .pathMatchers("/public/**").permitAll()
                        .anyExchange().authenticated()
                )
                .oauth2ResourceServer(oaauth2 -> oaauth2
                        .jwt(jwt -> jwt
                                .jwtAuthenticationConverter(new JwtAuthConverter()
                                )
                        )
                );
        return httpSecurity.build();
    }

}
