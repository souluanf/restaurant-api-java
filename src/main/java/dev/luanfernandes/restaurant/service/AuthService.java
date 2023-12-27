package dev.luanfernandes.restaurant.service;

import dev.luanfernandes.restaurant.domain.request.TokenRequest;
import dev.luanfernandes.restaurant.domain.response.TokenResponse;

public interface AuthService {

    TokenResponse getToken(TokenRequest user);
}
