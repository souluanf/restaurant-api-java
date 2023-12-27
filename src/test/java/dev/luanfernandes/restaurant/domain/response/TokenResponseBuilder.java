package dev.luanfernandes.restaurant.domain.response;

public class TokenResponseBuilder {

    public static TokenResponse getTokenResponse() {
        return new TokenResponse(
                "access_token",
                300,
                1800,
                "refresh_token",
                "Bearer",
                0,
                "e3c5e649-4be1-421d-a5bc-20a12a6a4fb2",
                "email profile");
    }
}
