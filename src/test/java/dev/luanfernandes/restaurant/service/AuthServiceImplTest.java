package dev.luanfernandes.restaurant.service;

import static dev.luanfernandes.restaurant.domain.request.TokenRequestBuilder.getTokenRequest;
import static dev.luanfernandes.restaurant.domain.response.TokenResponseBuilder.getTokenResponse;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import dev.luanfernandes.restaurant.domain.request.TokenRequest;
import dev.luanfernandes.restaurant.domain.response.TokenResponse;
import dev.luanfernandes.restaurant.service.impl.AuthServiceImpl;
import dev.luanfernandes.restaurant.webclient.KeycloakClient;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class AuthServiceImplTest {

    @InjectMocks
    private AuthServiceImpl authService;

    @Mock
    private KeycloakClient keycloakClient;

    @Test
    void getTokenShouldCallKeycloakClientAndReturnResponse() {
        var tokenRequest = getTokenRequest();
        var expectedResponse = getTokenResponse();
        when(keycloakClient.getToken(any(TokenRequest.class))).thenReturn(expectedResponse);
        TokenResponse actualResponse = authService.getToken(tokenRequest);
        assertNotNull(actualResponse);
        assertEquals(expectedResponse.accessToken(), actualResponse.accessToken());
        verify(keycloakClient).getToken(any(TokenRequest.class));
    }
}
