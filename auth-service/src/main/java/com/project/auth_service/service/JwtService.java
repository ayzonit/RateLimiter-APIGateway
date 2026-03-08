package com.project.auth_service.service;

import com.project.auth_service.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.oauth2.jwt.JwtClaimsSet;
import org.springframework.security.oauth2.jwt.JwtEncoder;
import org.springframework.security.oauth2.jwt.JwtEncoderParameters;
import org.springframework.stereotype.Service;

import java.time.Instant;

@Service
public class JwtService {

    @Autowired
    JwtEncoder jwtEncoder;

    public String generateToken(User user) {

        Instant now = Instant.now();

        JwtClaimsSet claims = JwtClaimsSet.builder()
                .issuer("http://auth-service:8081")
                .subject(user.getUserId().toString())
                .claim("role", user.getRole().name())
                .issuedAt(now)
                .expiresAt(now.plusSeconds(900))
                .build();

        System.out.println("Claims before encoding: " + claims.getClaims());
        System.out.println("Issuer: " + claims.getIssuer());

        JwtEncoderParameters jwtEncoderParameters = JwtEncoderParameters.from(claims);

        return jwtEncoder.encode(jwtEncoderParameters).getTokenValue();
    }
}
