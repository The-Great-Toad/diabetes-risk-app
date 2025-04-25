package com.diabetesrisk.auth_service.service;

import com.diabetesrisk.auth_service.model.InvalidCredentialException;
import com.diabetesrisk.auth_service.model.LoginRequest;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.MacAlgorithm;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.util.Date;
import java.util.Objects;

/**
 * <a href="https://github.com/jwtk/jjwt?tab=readme-ov-file#creating-a-jwt">jsonwebtoken documentation</a>
 */
@Service
public class JwtService {

    private static final Logger log = LoggerFactory.getLogger(JwtService.class);

    private final UserDetailsService userDetailsService;

    private final PasswordEncoder passwordEncoder;

    private final MacAlgorithm alg = Jwts.SIG.HS512;
    private final SecretKey key = alg.key().build();

    @Value("${jwt.expiration.time:1000 * 60 * 60}") // Default to 1 hour
    private int expirationTime;

    public JwtService(UserDetailsService userDetailsService, PasswordEncoder passwordEncoder) {
        this.userDetailsService = userDetailsService;
        this.passwordEncoder = passwordEncoder;
    }

    /**
     * Authenticate the user by validating credentials and generating a JWT token
     *
     * @param request login request
     * @return JWT token
     */
    public String authenticateUser(LoginRequest request) {
        UserDetails userDetails = userDetailsService.loadUserByUsername(request.getUsername());

        if (Objects.nonNull(userDetails) && passwordEncoder.matches(request.getPassword(), userDetails.getPassword())) {
            return generateToken(userDetails);

        } else {
            throw new InvalidCredentialException();
        }
    }

    /**
     * Generate a JWT token
     * <a href="https://github.com/jwtk/jjwt?tab=readme-ov-file#jwt-signed-with-hmac">JWT Signed with HMAC</a>
     *
     * @param user user to authenticate
     * @return JWT token
     */
    private String generateToken(UserDetails user) {
        return Jwts.builder()
                .subject(user.getUsername())
                .issuedAt(new Date())
                .expiration(new Date(System.currentTimeMillis() + expirationTime))
                .signWith(key, alg)
                .compact();
    }

    /**
     * Parse the JWT token for validation
     *
     * @param token JWT token to validate
     * @return true if the token is valid, false otherwise
     */
    public Boolean validateToken(String token) {
        try {
            String username = getUsernameFromToken(token);
            UserDetails userDetails = userDetailsService.loadUserByUsername(username);

            /* Check if the username matches userDetails */
            if (Objects.isNull(userDetails) || Objects.isNull(username) || username.isEmpty() || !userDetails.getUsername().equals(username)) {
                log.error("Token username is invalid: {}", username);
                return false;
            }

            Date expiration = getExpirationDateFromToken(token);

            log.info("Username: {}, Expiration: {}", username, expiration);
            log.info("User details: {}", userDetails);

            /* Check token expiration date */
            if (Objects.isNull(expiration) || !expiration.after(new Date())) {
                log.error("Token is expired for user: {}", username);
                return false;
            }

        } catch (Exception e) {
            log.error(e.getMessage(), e);
        }
        log.info("Token is valid: {}", token);
        return true;
    }

    /**
     * Extract the username from the JWT token
     *
     * @param token JWT token to extract the username from
     * @return username
     */
    public String getUsernameFromToken(String token) {
        String username = getClaims(token).getSubject();
        log.debug("Username: {}", username);
        return username;
    }

    /**
     * Extract the expiration date from the JWT token
     *
     * @param token JWT token to extract the expiration date from
     * @return expiration date
     */
    public Date getExpirationDateFromToken(String token) {
        Date expiration = getClaims(token).getExpiration();
        log.debug("Expiration date: {}", expiration);
        return expiration;
    }

    /**
     * Get the claims from the JWT token
     *
     * @param token JWT token to extract the claims from
     * @return claims
     */
    private Claims getClaims(String token) {
        Claims claims = Jwts
                .parser()
                .verifyWith(key)
                .build()
                .parseSignedClaims(token)
                .getPayload();
        log.debug("Claims: {}", claims);
        return claims;
    }
}