package dev.luanfernandes.restaurant.controller.impl;

import dev.luanfernandes.restaurant.controller.AuthController;
import dev.luanfernandes.restaurant.domain.request.TokenRequest;
import dev.luanfernandes.restaurant.domain.response.TokenResponse;
import dev.luanfernandes.restaurant.service.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class AuthControllerImpl implements AuthController {

    private final AuthService authService;

    @Override
    public ResponseEntity<TokenResponse> getToken(TokenRequest user) {
        return ResponseEntity.ok(authService.getToken(user));
    }
}
