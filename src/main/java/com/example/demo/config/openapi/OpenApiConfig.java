package com.example.demo.config.openapi;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeIn;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeType;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.security.SecurityScheme;
import io.swagger.v3.oas.annotations.servers.Server;
import org.springframework.context.annotation.Configuration;

@Configuration
@SecurityScheme(name = "API_KEY",
        type = SecuritySchemeType.APIKEY,
        in = SecuritySchemeIn.HEADER,
        paramName = "X-API-KEY",
        description = "API Key Authentication")
@OpenAPIDefinition(
        info = @Info(title = "Ticket API", version = "v1"),
        security = {@SecurityRequirement(name = "API_KEY")},
        servers = {@Server(url = "${api.swagger-ui.context}", description = "${spring.profiles.active:default}")})
public class OpenApiConfig {
}
