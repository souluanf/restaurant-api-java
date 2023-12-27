package dev.luanfernandes.restaurant.controller;

import static dev.luanfernandes.restaurant.common.constants.PathConstants.AUTH_TOKEN_V1;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

import dev.luanfernandes.restaurant.domain.request.TokenRequest;
import dev.luanfernandes.restaurant.domain.response.TokenResponse;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

@RequestMapping(produces = APPLICATION_JSON_VALUE)
public interface AuthController {

    @PostMapping(value = AUTH_TOKEN_V1, consumes = APPLICATION_JSON_VALUE)
    ResponseEntity<TokenResponse> getToken(@Valid @RequestBody TokenRequest user);
}
