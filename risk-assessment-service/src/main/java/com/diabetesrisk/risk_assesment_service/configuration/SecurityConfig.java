package com.diabetesrisk.risk_assesment_service.configuration;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpStatusCode;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.Base64;

import static org.springframework.security.config.Customizer.withDefaults;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    private static final Logger logger = LoggerFactory.getLogger(SecurityConfig.class);

    @Value("${spring.patient.client.url}")
    private String patientClientUrl;

    @Value("${spring.notes.client.url}")
    private String notesClientUrl;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .csrf(AbstractHttpConfigurer::disable)
                .authorizeHttpRequests((authorize) -> authorize
                        .anyRequest().authenticated()
                )
                .httpBasic(withDefaults());

        return http.build();
    }

    @Bean
    public UserDetailsService userDetailsService() {
        PasswordEncoder encoder = PasswordEncoderFactories.createDelegatingPasswordEncoder();
        UserDetails user = User.builder()
                .username("user")
                .password(encoder.encode("password"))
                .roles("USER")
                .build();

        return new InMemoryUserDetailsManager(user);
    }

    @Bean(name = "patientClient")
    public WebClient patientClient() {
        logger.info("Patient Client URL: {}", patientClientUrl);
        return WebClient.builder()
                .baseUrl(patientClientUrl)
                .defaultHeader("Content-Type", "application/json")
                .defaultHeader("Accept", "application/json")
                .defaultHeader("Authorization", "Basic " + Base64.getEncoder().encodeToString("user:password".getBytes()))
                .defaultStatusHandler(HttpStatusCode::isError,
                        clientResponse -> {
                            logger.error("Error response from patient client: {} - {}", clientResponse.statusCode(), clientResponse);
                            throw new RuntimeException("Error response from patient client");
                        })
                .build();
    }

    @Bean(name = "notesClient")
    public WebClient notesClient() {
        logger.info("Notes Client URL: {}", notesClientUrl);
        return WebClient.builder()
                .baseUrl(notesClientUrl)
                .defaultHeader("Content-Type", "application/json")
                .defaultHeader("Accept", "application/json")
                .defaultHeader("Authorization", "Basic " + Base64.getEncoder().encodeToString("user:password".getBytes()))
                .defaultStatusHandler(HttpStatusCode::isError,
                        clientResponse -> {
                            logger.error("Error response from notes client: {} - {}", clientResponse.statusCode(), clientResponse);
                            throw new RuntimeException("Error response from notes client");
                        })
                .build();
    }
}
