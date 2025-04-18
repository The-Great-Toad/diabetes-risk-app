package com.diabetesrisk.notes_service.configuration;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeIn;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeType;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.security.SecurityScheme;
import io.swagger.v3.oas.annotations.servers.Server;

@OpenAPIDefinition(
        info = @Info(
                title = "Notes Service API",
                version = "1.0",
                description = "API for managing patient notes"
        ),
        servers = {
                @Server(
                        url = "http://localhost:8082/api",
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
public class OpenApiConfig {
// https://springdoc.org/#what-is-a-proper-way-to-set-up-swagger-ui-to-use-provided-spec-yml
//    @Bean
//    SpringDocConfiguration springDocConfiguration(){
//        return new SpringDocConfiguration();
//    }
//
//    @Bean
//    SpringDocConfigProperties springDocConfigProperties() {
//        return new SpringDocConfigProperties();
//    }
//
//    @Bean
//    ObjectMapperProvider objectMapperProvider(SpringDocConfigProperties springDocConfigProperties){
//        return new ObjectMapperProvider(springDocConfigProperties);
//    }
}
