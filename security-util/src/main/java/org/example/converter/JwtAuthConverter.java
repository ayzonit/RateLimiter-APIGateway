package org.example.converter;

import org.example.constant.JwtClaims;
import org.springframework.core.convert.converter.Converter;
import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import reactor.core.publisher.Mono;

import java.util.Collections;
import java.util.List;

public class JwtAuthConverter implements Converter<Jwt, Mono<AbstractAuthenticationToken>> {

    @Override
    public Mono<AbstractAuthenticationToken> convert(Jwt jwt) {

        String role = jwt.getClaimAsString(JwtClaims.ROLE);
        List<SimpleGrantedAuthority> authorities = Collections.emptyList();

        if (role != null) {
            authorities = List.of(
                    new SimpleGrantedAuthority("ROLE_" + role)
            );
        }
        AbstractAuthenticationToken authenticationToken =
                new JwtAuthenticationToken(jwt, authorities, jwt.getSubject());
        return Mono.just(authenticationToken);
    }
}
