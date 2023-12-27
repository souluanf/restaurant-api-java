package dev.luanfernandes.restaurant.config.security;

import static dev.luanfernandes.restaurant.common.constants.PathConstants.AUTH_TOKEN_V1;
import static dev.luanfernandes.restaurant.common.constants.PathConstants.ORDERS_BY_ID_V1;
import static dev.luanfernandes.restaurant.common.constants.PathConstants.ORDERS_V1;
import static dev.luanfernandes.restaurant.common.constants.PathConstants.PRODUCTS_BY_CATEGORY_V1;
import static dev.luanfernandes.restaurant.common.constants.PathConstants.PRODUCTS_BY_ID_V1;
import static dev.luanfernandes.restaurant.common.constants.PathConstants.PRODUCTS_V1;
import static dev.luanfernandes.restaurant.common.constants.PathConstants.PUBLISHER_V1;
import static org.springframework.http.HttpMethod.GET;
import static org.springframework.http.HttpMethod.POST;
import static org.springframework.http.HttpMethod.PUT;
import static org.springframework.security.config.Customizer.withDefaults;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
public class WebSecurityConfig {
    private static final String ADMIN_ROLE = "ADMIN";

    private static final String[] ALLOWED_PATHS = {
        "/actuator/**", "/swagger-ui.html", "/swagger-ui/**", "/v3/api-docs/**", AUTH_TOKEN_V1
    };

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        return http.csrf(AbstractHttpConfigurer::disable)
                .authorizeHttpRequests(auth -> auth.requestMatchers(ALLOWED_PATHS)
                        .permitAll()
                        .requestMatchers(POST, PRODUCTS_V1)
                        .hasRole(ADMIN_ROLE)
                        .requestMatchers(GET, PRODUCTS_V1)
                        .hasRole(ADMIN_ROLE)
                        .requestMatchers(GET, PRODUCTS_BY_ID_V1)
                        .hasRole(ADMIN_ROLE)
                        .requestMatchers(PUT, PRODUCTS_BY_ID_V1)
                        .hasRole(ADMIN_ROLE)
                        .requestMatchers(GET, PRODUCTS_BY_CATEGORY_V1)
                        .hasRole(ADMIN_ROLE)
                        .requestMatchers(POST, PUBLISHER_V1)
                        .hasRole(ADMIN_ROLE)
                        .requestMatchers(POST, ORDERS_V1)
                        .hasRole(ADMIN_ROLE)
                        .requestMatchers(GET, ORDERS_V1)
                        .hasRole(ADMIN_ROLE)
                        .requestMatchers(GET, ORDERS_BY_ID_V1)
                        .hasRole(ADMIN_ROLE)
                        .requestMatchers(PUT, ORDERS_BY_ID_V1)
                        .hasRole(ADMIN_ROLE)
                        .anyRequest()
                        .denyAll())
                .oauth2ResourceServer(oauth2 -> oauth2.jwt(jwt -> jwt.jwtAuthenticationConverter(new JwtConverter())))
                .cors(withDefaults())
                .build();
    }
}
