package dev.luanfernandes.restaurant.controller;

import static dev.luanfernandes.restaurant.common.constants.PathConstants.AUTH_TOKEN_V1;
import static dev.luanfernandes.restaurant.domain.request.TokenRequestBuilder.getTokenRequest;
import static dev.luanfernandes.restaurant.domain.response.TokenResponseBuilder.getTokenResponse;
import static java.util.stream.Stream.of;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import dev.luanfernandes.restaurant.common.exceptions.ExceptionHandlerAdvice;
import dev.luanfernandes.restaurant.config.security.WebSecurityConfig;
import dev.luanfernandes.restaurant.controller.impl.AuthControllerImpl;
import dev.luanfernandes.restaurant.domain.request.TokenRequest;
import dev.luanfernandes.restaurant.service.AuthService;
import java.util.stream.Stream;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.web.servlet.MockMvc;

@ActiveProfiles("test")
@WebMvcTest
@ContextConfiguration(classes = {AuthControllerImpl.class, WebSecurityConfig.class, ExceptionHandlerAdvice.class})
@WithMockUser(roles = {"ADMIN"})
@DisplayName("Tests for AuthController")
class AuthControllerTest {

    @Autowired
    private MockMvc mvc;

    @MockBean
    private AuthService authService;

    @Test
    @DisplayName("Should return token response when request is valid")
    void shouldReturnTokenResponseWhenRequestIsValid() throws Exception {
        var tokenRequest = getTokenRequest();
        var tokenResponse = getTokenResponse();
        when(authService.getToken(any(TokenRequest.class))).thenReturn(tokenResponse);

        mvc.perform(post(AUTH_TOKEN_V1)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(tokenRequest)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.access_token").value(tokenResponse.accessToken()))
                .andExpect(jsonPath("$.expires_in").value(tokenResponse.expiresIn()))
                .andExpect(jsonPath("$.refresh_expires_in").value(tokenResponse.refreshExpiresIn()))
                .andExpect(jsonPath("$.refresh_token").value(tokenResponse.refreshToken()))
                .andExpect(jsonPath("$.token_type").value(tokenResponse.tokenType()))
                .andExpect(jsonPath("$.not-before-policy").value(tokenResponse.notBeforePolicy()));
    }

    @ParameterizedTest
    @MethodSource("invalidTokenRequestsProvider")
    @DisplayName("Should return 400 BAD REQUEST for invalid TokenRequests")
    void shouldReturnBadRequestForInvalidTokenRequests(TokenRequest request) throws Exception {
        mvc.perform(post(AUTH_TOKEN_V1)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(request)))
                .andExpect(status().isBadRequest());
    }

    static Stream<TokenRequest> invalidTokenRequestsProvider() {
        return of(
                new TokenRequest("", "password"),
                new TokenRequest("username", ""),
                new TokenRequest(null, "password"),
                new TokenRequest("username", null),
                new TokenRequest(null, null));
    }
}
