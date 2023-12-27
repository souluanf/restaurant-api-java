package dev.luanfernandes.restaurant.domain.request;

public class TokenRequestBuilder {

    public static TokenRequest getTokenRequest() {
        return new TokenRequest("username", "password", "client_id");
    }
}
