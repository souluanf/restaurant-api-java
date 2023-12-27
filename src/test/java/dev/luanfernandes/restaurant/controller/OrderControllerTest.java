package dev.luanfernandes.restaurant.controller;

import static dev.luanfernandes.restaurant.common.constants.PathConstants.ORDERS_BY_ID_V1;
import static dev.luanfernandes.restaurant.common.constants.PathConstants.ORDERS_V1;
import static dev.luanfernandes.restaurant.common.constants.TestConstants.ID;
import static dev.luanfernandes.restaurant.common.constants.TestConstants.TOKEN;
import static dev.luanfernandes.restaurant.domain.request.OrderRequestBuilder.getOrderRequest;
import static dev.luanfernandes.restaurant.domain.request.OrderUpdateQuantityRequestBuilder.getOrderUpdateQuantityRequest;
import static dev.luanfernandes.restaurant.domain.response.OrderResponseBuilder.getOrderResponse;
import static dev.luanfernandes.restaurant.domain.response.OrderResponseBuilder.getOrderResponseList;
import static java.lang.String.valueOf;
import static java.util.Collections.emptyMap;
import static org.junit.jupiter.params.provider.Arguments.of;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;
import static org.springframework.http.HttpHeaders.AUTHORIZATION;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import dev.luanfernandes.restaurant.common.exceptions.ExceptionHandlerAdvice;
import dev.luanfernandes.restaurant.config.security.WebSecurityConfig;
import dev.luanfernandes.restaurant.controller.impl.OrderControllerImpl;
import dev.luanfernandes.restaurant.domain.request.OrderRequest;
import dev.luanfernandes.restaurant.domain.request.OrderUpdateQuantityRequest;
import dev.luanfernandes.restaurant.facade.OrderFacade;
import java.util.stream.Stream;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.web.servlet.MockMvc;

@ActiveProfiles("test")
@WebMvcTest
@ContextConfiguration(classes = {OrderControllerImpl.class, WebSecurityConfig.class, ExceptionHandlerAdvice.class})
@WithMockUser(roles = {"ADMIN"})
@DisplayName("Tests for OrderController")
class OrderControllerTest {

    @Autowired
    private MockMvc mvc;

    @MockBean
    private OrderFacade facade;

    private static ObjectMapper objectMapper;

    @BeforeAll
    static void setUp() {
        objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());
    }

    @Test
    @DisplayName("Should create a product and return CREATED status")
    void shouldCreateProductSuccessfully() throws Exception {
        var orderRequest = getOrderRequest();
        var orderResponse = getOrderResponse();
        when(facade.create(any(OrderRequest.class))).thenReturn(orderResponse);
        mvc.perform(post(ORDERS_V1)
                        .contentType(APPLICATION_JSON_VALUE)
                        .header(AUTHORIZATION, TOKEN)
                        .content(new ObjectMapper().writeValueAsString(orderRequest))
                        .with(csrf()))
                .andExpect(status().isCreated());
    }

    @Test
    @DisplayName("Should create a product and return ACCEPTED status")
    void shouldReturnAcceptedStatus() throws Exception {
        var orderResponse = getOrderResponse();
        when(facade.getById(any())).thenReturn(orderResponse);
        mvc.perform(get(ORDERS_BY_ID_V1.replace("{id}", valueOf(ID)))
                        .contentType(APPLICATION_JSON_VALUE)
                        .header(AUTHORIZATION, TOKEN)
                        .content(objectMapper.writeValueAsString(orderResponse))
                        .accept(APPLICATION_JSON_VALUE)
                        .with(csrf()))
                .andExpect(status().isOk());
    }

    @ParameterizedTest
    @MethodSource("invalidOrderRequests")
    @DisplayName("Should return BAD REQUEST for invalid order data")
    void shouldReturnBadRequestForInvalidProductData(OrderRequest request) throws Exception {
        mvc.perform(post(ORDERS_V1)
                        .contentType(APPLICATION_JSON_VALUE)
                        .header(AUTHORIZATION, TOKEN)
                        .content(new ObjectMapper().writeValueAsString(request))
                        .with(csrf()))
                .andExpect(status().isBadRequest());
    }

    public static Stream<Arguments> invalidOrderRequests() {
        return Stream.of(of(new OrderRequest(null)), of(new OrderRequest(emptyMap())));
    }

    @Test
    @DisplayName("Should create a product and return ACCEPTED status")
    void shouldCreateProduct() throws Exception {
        var orderResponseList = getOrderResponseList();
        when(facade.getAll()).thenReturn(orderResponseList);
        mvc.perform(get(ORDERS_V1)
                        .contentType(APPLICATION_JSON_VALUE)
                        .header(AUTHORIZATION, TOKEN)
                        .content(objectMapper.writeValueAsString(orderResponseList))
                        .accept(APPLICATION_JSON_VALUE)
                        .with(csrf()))
                .andExpect(status().isOk());
    }

    @Test
    @DisplayName("Should update a product and return ACCEPTED status")
    void shouldUpdateProduct() throws Exception {
        var orderUpdateQuantityRequest = getOrderUpdateQuantityRequest();
        doNothing().when(facade).updateProductQuantityInOrder(any(Long.class), any(OrderUpdateQuantityRequest.class));
        mvc.perform(put(ORDERS_BY_ID_V1.replace("{id}", valueOf(ID)))
                        .contentType(APPLICATION_JSON_VALUE)
                        .header(AUTHORIZATION, TOKEN)
                        .content(new ObjectMapper().writeValueAsString(orderUpdateQuantityRequest))
                        .with(csrf()))
                .andExpect(status().isNoContent());
    }
}
