package com.diabetesrisk.patient_service.configuration;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeIn;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeType;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.security.SecurityScheme;
import io.swagger.v3.oas.annotations.servers.Server;

@OpenAPIDefinition(
        info = @Info(
                title = "Risk Assessment Service API",
                version = "1.0",
                description = "API for assessing diabetes risk based on patient data and notes."
        ),
        servers = {
                @Server(
                        url = "http://localhost:8083/api",
                        description = "Local server"
                )
        },
        security = {
                @SecurityRequirement(name = "basicAuth")
        }
)
@SecurityScheme(
        name = "basicAuth",
        description = "Basic authentication for the API",
        scheme = "basic",
        type = SecuritySchemeType.HTTP,
        bearerFormat = "basic",
        in = SecuritySchemeIn.HEADER
)
public class OpenApiConfig {}
