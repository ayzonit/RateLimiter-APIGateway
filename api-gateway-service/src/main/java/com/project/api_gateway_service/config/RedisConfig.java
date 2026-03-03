package com.project.api_gateway_service.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;

import org.springframework.data.redis.core.script.RedisScript;

import java.util.List;

@Configuration
public class RedisConfig {

    @Bean
    public RedisScript<List> tokenBucketScript() {
        return RedisScript.of(new ClassPathResource("lua/token_bucket.lua"),
                List.class
        );
    }
}
