package com.e_commerce.api_gateway.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class GatewayConfig {

    @Value("${services.users.uri}")
    private String USERS_SERVICE;

    @Bean
    public RouteLocator customRouteLocator(RouteLocatorBuilder builder) {
        return builder.routes()
                .route("users_service", r -> r.path("/users/**", "/auth/**", "/swagger-ui/**", "/v3/api-docs/**", "/common/**", "/restricted/**")
                        .uri(USERS_SERVICE))
                .build();
    }

}
