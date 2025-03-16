package com.diabetesrisk.patient_service.confirguration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

//@Configuration
public class CorsConfig {

    @Bean
    public WebMvcConfigurer corsConfigurer() {
        return new WebMvcConfigurer() {
            @Override
            public void addCorsMappings(CorsRegistry registry) {
                registry.addMapping("/**") // Autorise tous les endpoints
                        .allowedOrigins("*") // Autorise le frontend Angular
//                        .allowedMethods(HttpMethod.GET.name(), HttpMethod.POST.name(), HttpMethod.PUT.name()) // Autorise les méthodes HTTP
                        .allowedMethods("*") // Autorise les méthodes HTTP
                        .allowedHeaders("*"); // Autorise tous les en-têtes
//                        .allowCredentials(true); // Autorise les cookies et les en-têtes d'authentification
            }
        };
    }
}
