package dev.luanfernandes.restaurant.service.impl;

import dev.luanfernandes.restaurant.domain.request.TokenRequest;
import dev.luanfernandes.restaurant.domain.response.TokenResponse;
import dev.luanfernandes.restaurant.service.AuthService;
import dev.luanfernandes.restaurant.webclient.KeycloakClient;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {

    private final KeycloakClient webClient;

    @Override
    public TokenResponse getToken(TokenRequest tokenRequest) {
        return webClient.getToken(tokenRequest);
    }
}
