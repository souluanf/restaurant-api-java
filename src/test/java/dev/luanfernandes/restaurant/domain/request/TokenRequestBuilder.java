package dev.luanfernandes.restaurant.domain.request;

import lombok.experimental.UtilityClass;

@UtilityClass
public class TokenRequestBuilder {

    public static TokenRequest getTokenRequest() {
        return new TokenRequest("username", "password");
    }
}
