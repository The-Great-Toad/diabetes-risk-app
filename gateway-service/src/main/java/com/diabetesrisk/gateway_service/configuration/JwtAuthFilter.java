package com.diabetesrisk.gateway_service.configuration;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

@Component
public class JwtAuthFilter implements GlobalFilter {

    private static final Logger log = LoggerFactory.getLogger(JwtAuthFilter.class);

    private final WebClient authClient;

    @Value("${auth.service.base.url}")
    private String authServiceBaseUrl;

    @Value("${auth.service.login.uri}")
    private String authServiceLoginUri;

    @Value("${auth.service.validate.uri}")
    private String authServiceValidateUri;

    @Value("${auth.validation.header}")
    private String authValidationHeader;

    public JwtAuthFilter(WebClient authClient) {
        this.authClient = authClient;
    }

    /**
     * Other than login request, checks for a valid JWT token.
     */
    @Override
    @Order(Ordered.HIGHEST_PRECEDENCE)
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        String uri = exchange.getRequest().getURI().toString();
        log.info("Request URI: {}", uri);
        log.info("Auth Service Login URL: {}{}", authServiceBaseUrl, authServiceLoginUri);

        /* Check if login attempt */
        if (uri.contains(authServiceLoginUri)) {
            log.info("Skipping authentication for login endpoint. Redirecting to Auth Service.");
            return chain.filter(exchange);
        }

        log.info("Processing request for other endpoints.");
        String token = exchange.getRequest().getHeaders().getFirst("Authorization");
        log.info("Authorization header: {}", token);

        if (token == null || !token.startsWith("Bearer ")) {
            exchange.getResponse().setStatusCode(HttpStatus.UNAUTHORIZED);
            return exchange.getResponse().setComplete();
        }

        log.info("Auth Service Validate URL: {}{}", authServiceBaseUrl, authServiceValidateUri);

        return validateToken(exchange, chain, token);
    }

    /**
     * <pre>
     * Validates the token with the Auth Service.
     * If valid, it adds a header to the request and continues the filter chain.
     * If invalid, it sets the response status to UNAUTHORIZED.
     * </pre>
     */
    private Mono<Void> validateToken(ServerWebExchange exchange, GatewayFilterChain chain, String token) {
        return authClient
                .get()
                .uri(authServiceValidateUri)
                .header("Authorization", token)
                .retrieve()
                .bodyToMono(Boolean.class)
                .flatMap(isValid -> {

                    if (Boolean.TRUE.equals(isValid)) {
                        log.info("Token is valid");

                        // Créer une nouvelle requête avec l'entête modifié
                        ServerHttpRequest mutatedRequest = exchange.getRequest()
                                .mutate()
                                .header(authValidationHeader, "true")
                                .build();

                        // Réinjecter la requête modifiée dans l'échange
                        ServerWebExchange mutatedExchange = exchange.mutate()
                                .request(mutatedRequest)
                                .build();

                        log.info("Modified request with validation header: {}", mutatedRequest.getHeaders().containsKey(authValidationHeader));
                        return chain.filter(mutatedExchange);
                    } else {
                        log.warn("Token is invalid");
                        exchange.getResponse().setStatusCode(HttpStatus.UNAUTHORIZED);
                        return exchange.getResponse().setComplete();
                    }
                })
                .onErrorResume(e -> {
                    log.error("Error connecting to Auth Service: {}", e.getMessage());
                    exchange.getResponse().setStatusCode(HttpStatus.SERVICE_UNAVAILABLE);
                    return exchange.getResponse().setComplete();
                });
    }
}