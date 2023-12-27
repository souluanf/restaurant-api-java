package dev.luanfernandes.restaurant.webclient;

import static org.springframework.http.MediaType.APPLICATION_FORM_URLENCODED;

import dev.luanfernandes.restaurant.domain.request.TokenRequest;
import dev.luanfernandes.restaurant.domain.response.TokenResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Component;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

@Component
@RequiredArgsConstructor
public class KeycloakClient {

    @Value("${spring.security.oauth2.resource-server.jwt.token-uri}")
    private String tokenUri;

    private final RestTemplate restTemplate;

    public TokenResponse getToken(TokenRequest tokenRequest) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(APPLICATION_FORM_URLENCODED);
        MultiValueMap<String, String> formData = new LinkedMultiValueMap<>();
        formData.add("client_id", tokenRequest.client_id());
        formData.add("username", tokenRequest.username());
        formData.add("password", tokenRequest.password());
        formData.add("grant_type", "password");
        HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<>(formData, headers);
        return restTemplate.postForObject(tokenUri, request, TokenResponse.class);
    }
}
