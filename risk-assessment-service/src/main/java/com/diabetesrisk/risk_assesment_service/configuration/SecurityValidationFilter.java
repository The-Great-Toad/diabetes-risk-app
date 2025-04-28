package com.diabetesrisk.risk_assesment_service.configuration;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Objects;

@Component
public class SecurityValidationFilter extends OncePerRequestFilter {

    private static final Logger log = LoggerFactory.getLogger(SecurityValidationFilter.class);

    private static final String AUTHORIZATION = "Authorization";
    private static final String BEARER_PREFIX = "Bearer ";
    private static final String AUTH_VALIDATION_HEADER = "X-User-Validated";

    /**
     * Checks for the presence of a valid JWT token and a custom validation header in the request headers.
     */
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        log.info("Processing request: {} {}", request.getMethod(), request.getRequestURI());

        if (!isValidAuthorizationHeader(request)) {
            response.sendError(HttpStatus.FORBIDDEN.value(), "Invalid or missing Authorization header");
            return;
        }

        if (!isValidValidationHeader(request)) {
            response.sendError(HttpStatus.FORBIDDEN.value(), "Missing validation header");
            return;
        }

        // Ajoute le token Bearer à la requête pour pouvoir le réutiliser dans les webClients
        String authorizationHeader = request.getHeader(AUTHORIZATION);
        if (authorizationHeader != null && authorizationHeader.startsWith(BEARER_PREFIX)) {
            String token = authorizationHeader.substring(BEARER_PREFIX.length());
            request.setAttribute(AUTHORIZATION, token);
        }

        filterChain.doFilter(request, response);
    }

    private boolean isValidAuthorizationHeader(HttpServletRequest request) {
        log.info("Checking for authorization header...");
        String authorizationHeader = request.getHeader(AUTHORIZATION);
        if (Objects.isNull(authorizationHeader) || !authorizationHeader.startsWith(BEARER_PREFIX)) {
            log.error("Invalid or missing Authorization header");
            return false;
        }
        log.info("Valid Authorization header found");
        return true;
    }

    private boolean isValidValidationHeader(HttpServletRequest request) {
        log.info("Checking for user validated header ({})...", AUTH_VALIDATION_HEADER);
        String validationHeader = request.getHeader(AUTH_VALIDATION_HEADER);
        if (Objects.isNull(validationHeader)) {
            log.error("Missing validation header: {}", AUTH_VALIDATION_HEADER);
            return false;
        }
        log.info("Valid validation header found: {}", AUTH_VALIDATION_HEADER);
        return true;
    }
}
