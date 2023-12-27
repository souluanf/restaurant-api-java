package dev.luanfernandes.restaurant.service;

import static dev.luanfernandes.restaurant.common.constants.TestConstants.CATEGORY;
import static dev.luanfernandes.restaurant.common.constants.TestConstants.ID;
import static dev.luanfernandes.restaurant.domain.entity.ProductBuilder.getProduct;
import static dev.luanfernandes.restaurant.domain.entity.ProductBuilder.getProductList;
import static dev.luanfernandes.restaurant.domain.enums.EventType.USER_CREATED;
import static dev.luanfernandes.restaurant.domain.enums.EventType.USER_UPDATED;
import static dev.luanfernandes.restaurant.domain.request.ProductRequestBuilder.getProductRequest;
import static dev.luanfernandes.restaurant.domain.response.ProductResponseBuilder.getProductResponse;
import static java.util.Optional.empty;
import static java.util.Optional.of;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertInstanceOf;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentCaptor.forClass;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import dev.luanfernandes.restaurant.domain.entity.Product;
import dev.luanfernandes.restaurant.domain.event.EventMessage;
import dev.luanfernandes.restaurant.domain.response.ProductResponse;
import dev.luanfernandes.restaurant.messaging.GenericProducer;
import dev.luanfernandes.restaurant.repository.ProductRepository;
import dev.luanfernandes.restaurant.service.impl.ProductServiceImpl;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.context.ActiveProfiles;

@ActiveProfiles("test")
@ExtendWith(MockitoExtension.class)
@DisplayName("Tests for ProductService")
class ProductServiceTest {

    @InjectMocks
    ProductServiceImpl service;

    @Mock
    private ProductRepository productRepository;

    @Mock
    private GenericProducer genericProducer;

    @Test
    @DisplayName("Should return a product response when call get product by id")
    void shouldReturnProductResponse_whenCallGetProductById() {
        var expectedProduct = getProduct();
        var expectedResponse = getProductResponse();
        when(productRepository.findById(ID)).thenReturn(of(expectedProduct));
        var actualResponse = service.getById(ID);
        assertNotNull(actualResponse);
        assertEquals(expectedResponse.id(), actualResponse.id());
        assertEquals(expectedResponse.name(), actualResponse.name());
        assertEquals(expectedResponse.price(), actualResponse.price());
        assertEquals(expectedResponse.category(), actualResponse.category());
        verify(productRepository).findById(ID);
    }

    @Test
    @DisplayName("Should create a product and send USER_CREATED event")
    void shouldCreateProductAndSendEvent() {
        var request = getProductRequest();
        var savedProduct = getProduct();
        var expectedResponse = getProductResponse();
        when(productRepository.save(any(Product.class))).thenReturn(savedProduct);
        var actualResponse = service.create(request);
        assertNotNull(actualResponse);
        assertEquals(expectedResponse.id(), actualResponse.id());
        verify(genericProducer).send(new EventMessage(USER_CREATED, actualResponse));
    }

    @Test
    @DisplayName("Should return all products")
    void shouldReturnAllProducts() {
        var products = getProductList();
        when(productRepository.findAll()).thenReturn(products);
        var responses = service.getAll();
        assertEquals(products.size(), responses.size());
    }

    @Test
    @DisplayName("Should return products by category")
    void shouldReturnProductsByCategory() {
        var category = CATEGORY;
        var products = getProductList();
        when(productRepository.findByCategory(category)).thenReturn(products);
        var responses = service.getByCategory(category);
        assertEquals(1, responses.size());
        assertEquals(category, responses.get(0).category());
    }

    @Test
    @DisplayName("Should throw EntityNotFoundException when product is not found")
    void shouldThrowExceptionWhenProductNotFound() {
        when(productRepository.findById(ID)).thenReturn(empty());
        assertThrows(EntityNotFoundException.class, () -> service.getById(ID));
    }

    @Test
    @DisplayName("Should return the product when product with given ID exists")
    void shouldReturnProduct_whenProductExists() {
        var expectedProduct = getProduct();
        when(productRepository.findById(ID)).thenReturn(of(expectedProduct));
        var actualProduct = service.getByProductId(ID);
        assertNotNull(actualProduct);
        assertEquals(expectedProduct.getId(), actualProduct.getId());
        assertEquals(expectedProduct.getName(), actualProduct.getName());
        assertEquals(expectedProduct.getPrice(), actualProduct.getPrice());
        assertEquals(expectedProduct.getCategory(), actualProduct.getCategory());
        verify(productRepository).findById(ID);
    }

    @Test
    @DisplayName("Should throw EntityNotFoundException when product with given ID does not exist")
    void shouldThrowEntityNotFoundException_whenProductDoesNotExist() {
        when(productRepository.findById(ID)).thenReturn(empty());
        assertThrows(EntityNotFoundException.class, () -> service.getByProductId(ID));
        verify(productRepository).findById(ID);
    }

    @Test
    @DisplayName("Should update a product and send USER_UPDATED event")
    void shouldUpdateProductAndSendEvent() {
        var updateRequest = getProductRequest();
        var existingProduct = getProduct();
        var updatedProduct = getProduct();
        when(productRepository.findById(ID)).thenReturn(of(existingProduct));
        when(productRepository.save(any(Product.class))).thenReturn(updatedProduct);
        service.update(ID, updateRequest);
        var productCaptor = forClass(Product.class);
        verify(productRepository).save(productCaptor.capture());
        var capturedProduct = productCaptor.getValue();
        assertEquals(updateRequest.name(), capturedProduct.getName());
        assertEquals(updateRequest.price(), capturedProduct.getPrice());
        assertEquals(updateRequest.category(), capturedProduct.getCategory());
        var eventCaptor = forClass(EventMessage.class);
        verify(genericProducer).send(eventCaptor.capture());
        var capturedEvent = eventCaptor.getValue();
        assertEquals(USER_UPDATED, capturedEvent.eventType());
        assertNotNull(capturedEvent.data());
        assertInstanceOf(ProductResponse.class, capturedEvent.data());
        var eventProductResponse = (ProductResponse) capturedEvent.data();
        assertEquals(ID, eventProductResponse.id());
    }
}
