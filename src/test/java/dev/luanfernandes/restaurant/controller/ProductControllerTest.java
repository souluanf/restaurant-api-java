package dev.luanfernandes.restaurant.controller;

import static dev.luanfernandes.restaurant.common.constants.PathConstants.PRODUCTS_BY_CATEGORY_V1;
import static dev.luanfernandes.restaurant.common.constants.PathConstants.PRODUCTS_BY_ID_V1;
import static dev.luanfernandes.restaurant.common.constants.PathConstants.PRODUCTS_V1;
import static dev.luanfernandes.restaurant.common.constants.TestConstants.CATEGORY;
import static dev.luanfernandes.restaurant.common.constants.TestConstants.ID;
import static dev.luanfernandes.restaurant.common.constants.TestConstants.NAME;
import static dev.luanfernandes.restaurant.common.constants.TestConstants.PRICE;
import static dev.luanfernandes.restaurant.common.constants.TestConstants.TOKEN;
import static dev.luanfernandes.restaurant.domain.request.ProductRequestBuilder.getProductRequest;
import static dev.luanfernandes.restaurant.domain.response.ProductResponseBuilder.getProductResponse;
import static dev.luanfernandes.restaurant.domain.response.ProductResponseBuilder.getProductResponseList;
import static java.lang.String.valueOf;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;
import static org.springframework.http.HttpHeaders.AUTHORIZATION;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import dev.luanfernandes.restaurant.common.exceptions.ExceptionHandlerAdvice;
import dev.luanfernandes.restaurant.config.security.WebSecurityConfig;
import dev.luanfernandes.restaurant.controller.impl.ProductControllerImpl;
import dev.luanfernandes.restaurant.domain.enums.ProductCategory;
import dev.luanfernandes.restaurant.domain.request.ProductRequest;
import dev.luanfernandes.restaurant.service.ProductService;
import jakarta.persistence.EntityNotFoundException;
import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Stream;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
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
@ContextConfiguration(classes = {ProductControllerImpl.class, WebSecurityConfig.class, ExceptionHandlerAdvice.class})
@WithMockUser(roles = {"ADMIN"})
@DisplayName("Tests for ProductController")
class ProductControllerTest {

    @Autowired
    private MockMvc mvc;

    @MockBean
    private ProductService service;

    @Test
    @DisplayName("Should create a product and return CREATED status")
    void shouldCreateProductSuccessfully() throws Exception {
        var validRequest = getProductRequest();
        var expected = getProductResponse();
        when(service.create(any(ProductRequest.class))).thenReturn(expected);
        mvc.perform(post(PRODUCTS_V1)
                        .contentType(APPLICATION_JSON_VALUE)
                        .header(AUTHORIZATION, TOKEN)
                        .content(new ObjectMapper().writeValueAsString(validRequest))
                        .with(csrf()))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.name").value(expected.name()))
                .andExpect(jsonPath("$.price").value(expected.price()))
                .andExpect(jsonPath("$.category").value(expected.category().name()));
    }

    @ParameterizedTest
    @MethodSource("invalidProductRequests")
    @DisplayName("Should return BAD REQUEST for invalid product data")
    void shouldReturnBadRequestForInvalidProductData(ProductRequest request) throws Exception {
        mvc.perform(post(PRODUCTS_V1)
                        .contentType(APPLICATION_JSON_VALUE)
                        .header(AUTHORIZATION, TOKEN)
                        .content(new ObjectMapper().writeValueAsString(request))
                        .with(csrf()))
                .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("Should create a product and return ACCEPTED status")
    void shouldCreateProduct() throws Exception {
        var productResponse = List.of(getProductResponse());
        when(service.getAll()).thenReturn(productResponse);
        mvc.perform(get(PRODUCTS_V1)
                        .contentType(APPLICATION_JSON_VALUE)
                        .header(AUTHORIZATION, "token")
                        .content(new ObjectMapper().writeValueAsString(getProductRequest()))
                        .accept(APPLICATION_JSON_VALUE)
                        .with(csrf()))
                .andExpect(status().isOk());
    }

    @Test
    @DisplayName("Should return products filtered by category with OK status")
    void shouldGetProductsByCategory() throws Exception {
        var filteredProducts = getProductResponseList();
        when(service.getByCategory(any(ProductCategory.class))).thenReturn(filteredProducts);
        mvc.perform(get(PRODUCTS_BY_CATEGORY_V1.replace("{category}", CATEGORY.name())))
                .andExpect(status().isOk());
    }

    @Test
    @DisplayName("Should return a product by id with OK status")
    void shouldGetProductById() throws Exception {
        var productResponse = getProductResponse();
        when(service.getById(any())).thenReturn(productResponse);
        mvc.perform(get(PRODUCTS_BY_ID_V1.replace("{id}", valueOf(ID)))
                        .contentType(APPLICATION_JSON_VALUE)
                        .header(AUTHORIZATION, TOKEN)
                        .content(new ObjectMapper().writeValueAsString(getProductRequest()))
                        .accept(APPLICATION_JSON_VALUE)
                        .with(csrf()))
                .andExpect(status().isOk());
    }

    @Test
    @DisplayName("Should return NOT FOUND status when product does not exist")
    void shouldReturnNotFoundWhenProductDoesNotExist() throws Exception {
        when(service.getById(any())).thenThrow(new EntityNotFoundException("Product not found"));
        mvc.perform(get(PRODUCTS_BY_ID_V1.replace("{id}", valueOf(ID))).with(csrf()))
                .andExpect(status().isNotFound());
    }

    @Test
    @DisplayName("Should update a product and return ACCEPTED status")
    void shouldUpdateProduct() throws Exception {
        var updateRequest = getProductRequest();
        doNothing().when(service).update(any(Long.class), any(ProductRequest.class));
        mvc.perform(put(PRODUCTS_BY_ID_V1.replace("{id}", valueOf(ID)))
                        .contentType(APPLICATION_JSON_VALUE)
                        .header(AUTHORIZATION, TOKEN)
                        .content(new ObjectMapper().writeValueAsString(updateRequest))
                        .with(csrf()))
                .andExpect(status().isNoContent());
    }

    public static Stream<ProductRequest> invalidProductRequests() {
        return Stream.of(
                new ProductRequest("", null, null),
                new ProductRequest(null, new BigDecimal("-10"), CATEGORY),
                new ProductRequest(NAME, null, null),
                new ProductRequest(" ", PRICE, null));
    }
}
