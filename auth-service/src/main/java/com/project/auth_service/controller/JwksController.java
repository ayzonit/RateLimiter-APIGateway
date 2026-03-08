package com.project.auth_service.controller;

import com.nimbusds.jose.jwk.JWKSet;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
public class JwksController {

    @Autowired
    JWKSet jwkSet;

    @GetMapping("/auth/jwks-json")
    public Map<String, Object> keys() {
        return jwkSet.toJSONObject();
    }
}
