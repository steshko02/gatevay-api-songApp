package com.epam.gateway.gatewayapi.config;

import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;

@Configuration
public class GatewayConfig {


    @Bean
    public RouteLocator routeLocator(RouteLocatorBuilder builder) {
        return builder.routes()
                .route("admin_service", r -> r.path("/admin1/**")
                        .and().method(HttpMethod.GET, HttpMethod.POST)
                        .uri("lb://ADMIN-APP"))
                .route("upload_app_router",
                        route -> route.path("/upload-app/**")
                                .and().method(HttpMethod.GET, HttpMethod.POST)
                                .filters(filter -> filter.stripPrefix(1))
                                .uri("lb://UPLOAD-APP"))
//                .route("upload_app_redirect",
//                        route->route.path("/upload-app/**")
//                                .and().method(HttpMethod.POST)
//                                .filters(filter-> filter.redirect(302,"/uploaddd-app?st=CLOUD_SYSTEM"))
//                                .uri("no://op"))
                .build();
    }
}
