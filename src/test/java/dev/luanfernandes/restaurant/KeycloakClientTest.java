package dev.luanfernandes.restaurant;

import static dev.luanfernandes.restaurant.common.constants.PathConstants.AUTH_TOKEN_V1;
import static dev.luanfernandes.restaurant.domain.request.TokenRequestBuilder.getTokenRequest;
import static dev.luanfernandes.restaurant.domain.response.TokenResponseBuilder.getTokenResponse;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.util.ReflectionTestUtils.setField;

import dev.luanfernandes.restaurant.domain.response.TokenResponse;
import dev.luanfernandes.restaurant.webclient.KeycloakClient;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpEntity;
import org.springframework.web.client.RestTemplate;

@ExtendWith(MockitoExtension.class)
class KeycloakClientTest {

    @InjectMocks
    private KeycloakClient keycloakClient;

    @Mock
    private RestTemplate restTemplate;

    @Test
    void getTokenShouldReturnTokenResponse() {
        var tokenRequest = getTokenRequest();
        var expectedResponse = getTokenResponse();
        when(restTemplate.postForObject(eq(AUTH_TOKEN_V1), any(HttpEntity.class), eq(TokenResponse.class)))
                .thenReturn(expectedResponse);
        setField(keycloakClient, "tokenUri", AUTH_TOKEN_V1);
        var actualResponse = keycloakClient.getToken(tokenRequest);
        assertNotNull(actualResponse);
        assertEquals(expectedResponse.accessToken(), actualResponse.accessToken());
        verify(restTemplate).postForObject(eq(AUTH_TOKEN_V1), any(HttpEntity.class), eq(TokenResponse.class));
    }
}
