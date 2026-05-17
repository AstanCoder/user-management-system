package com.example.contact.infrastructure.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.servers.Server;
import java.util.List;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * OpenAPI metadata for Swagger UI.
 */
@Configuration
public class OpenApiConfig {

    @Bean
    public OpenAPI contactListOpenApi(@Value("${server.port:8080}") int serverPort) {
        return new OpenAPI()
                .info(new Info()
                        .title("Contact List API")
                        .version("1.0.0")
                        .description(
                                "REST API for managing a contact list. All write operations validate and sanitize input.")
                        .contact(new Contact().name("Contact List Team").email("support@example.com")))
                .servers(List.of(new Server().url("http://localhost:" + serverPort).description("Local")));
    }
}
