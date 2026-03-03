package org.example.util;

import org.springframework.security.core.context.ReactiveSecurityContextHolder;
import reactor.core.publisher.Mono;

public final class SecurityUtil {

    private SecurityUtil() {};

    public static Mono<String> getCurrentUserId() {
        return ReactiveSecurityContextHolder.getContext()
                .map(securityContext -> securityContext.getAuthentication())
                .filter(authentication -> authentication != null && authentication.isAuthenticated())
                .map(authentication -> authentication.getName());
    }
}
