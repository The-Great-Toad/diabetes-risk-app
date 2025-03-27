package com.diabetesrisk.gateway_service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.net.URI;
import java.security.Principal;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@SpringBootApplication
@RestController
@CrossOrigin
public class GatewayServiceApplication {

	private static final Logger LOGGER = LoggerFactory.getLogger(GatewayServiceApplication.class);
	private static final String LOG_ID = "[GATEWAY-SERVICE]";

	public static void main(String[] args) {
		SpringApplication.run(GatewayServiceApplication.class, args);
	}

	@PostMapping("/login")
	public ResponseEntity<Authentication> login(Authentication authentication, @RequestHeader("Authorization") String authorization) {
		LOGGER.info("{} - User logged in: {} - Name: {} - {}", LOG_ID, authorization, authentication.getName(), authentication);
		return ResponseEntity.ok(authentication);
	}

//	@PostMapping("/logout")
//	public ResponseEntity<Void> logout() {
//		LOGGER.info("{} - User logged out", LOG_ID);
//		SecurityContextHolder.getContext().setAuthentication(null);
//		return ResponseEntity.ok().build();
//	}

}
