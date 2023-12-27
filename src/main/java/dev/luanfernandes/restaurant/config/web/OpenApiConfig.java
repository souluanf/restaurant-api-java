package dev.luanfernandes.restaurant.config.web;

import static io.swagger.v3.oas.annotations.enums.SecuritySchemeIn.HEADER;
import static io.swagger.v3.oas.annotations.enums.SecuritySchemeType.HTTP;
import static java.util.stream.Stream.of;

import io.swagger.v3.oas.annotations.ExternalDocumentation;
import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Contact;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.security.SecurityScheme;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.servers.Server;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@OpenAPIDefinition(
        info =
                @Info(
                        contact =
                                @Contact(
                                        name = "Luan Fernandes",
                                        email = "souluanf@icloud.com",
                                        url = "https://linkedin.com/in/souluanf"),
                        title = "restaurant-api",
                        version = "0.0.1",
                        description = "API for restaurant"),
        externalDocs =
                @ExternalDocumentation(
                        description = "GitHub repository",
                        url = "https://github.com/souluanf/restaurant-api-java"),
        security = {@SecurityRequirement(name = "bearerAuth")})
@SecurityScheme(
        name = "bearerAuth",
        description = "JWT auth description",
        scheme = "bearer",
        type = HTTP,
        bearerFormat = "JWT",
        in = HEADER)
@Configuration
// @SecurityScheme(name = "BearerAuthentication", type = HTTP, bearerFormat = "JWT", scheme = "bearer")
public class OpenApiConfig {

    @Value("${swagger-servers-urls}")
    private String[] swaggerServersUrls;

    @Bean
    public OpenAPI customOpenAPI() {
        OpenAPI openApi = new OpenAPI();
        of(swaggerServersUrls).forEach(url -> openApi.addServersItem(new Server().url(url)));
        return openApi;
    }
}
