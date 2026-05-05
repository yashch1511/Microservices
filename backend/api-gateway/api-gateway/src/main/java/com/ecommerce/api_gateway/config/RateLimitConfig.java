package com.ecommerce.api_gateway.config;

import java.util.Optional;

import org.springframework.cloud.gateway.filter.ratelimit.KeyResolver;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import reactor.core.publisher.Mono;

@Configuration
public class RateLimitConfig {

    @Bean
    public KeyResolver ipKeyResolver() {
        return exchange -> Mono.just(
                Optional.ofNullable(exchange.getRequest().getHeaders().getFirst("X-Forwarded-For"))
                        .map(value -> value.split(",")[0].trim())
                        .filter(value -> !value.isBlank())
                        .orElseGet(() -> Optional.ofNullable(exchange.getRequest().getRemoteAddress())
                                .map(address -> address.getAddress().getHostAddress())
                                .orElse("unknown-client"))
        );
    }
}
