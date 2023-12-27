package dev.luanfernandes.restaurant.service.impl;

import static dev.luanfernandes.restaurant.domain.enums.EventType.USER_CREATED;
import static dev.luanfernandes.restaurant.domain.enums.EventType.USER_UPDATED;

import dev.luanfernandes.restaurant.domain.entity.Product;
import dev.luanfernandes.restaurant.domain.enums.ProductCategory;
import dev.luanfernandes.restaurant.domain.event.EventMessage;
import dev.luanfernandes.restaurant.domain.request.ProductRequest;
import dev.luanfernandes.restaurant.domain.response.ProductResponse;
import dev.luanfernandes.restaurant.messaging.GenericProducer;
import dev.luanfernandes.restaurant.repository.ProductRepository;
import dev.luanfernandes.restaurant.service.ProductService;
import jakarta.persistence.EntityNotFoundException;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ProductServiceImpl implements ProductService {

    private final ProductRepository productRepository;

    private final GenericProducer genericProducer;

    @Override
    public ProductResponse create(ProductRequest request) {
        var product = this.productRequestToProduct(request);
        var productSaved = productRepository.save(product);
        ProductResponse productResponse = this.productToProductResponse(productSaved);
        genericProducer.send(new EventMessage(USER_CREATED, productResponse));
        return productResponse;
    }

    @Override
    public List<ProductResponse> getAll() {
        return productRepository.findAll().stream()
                .map(this::productToProductResponse)
                .toList();
    }

    @Override
    public ProductResponse getById(Long id) {
        return this.productToProductResponse(
                productRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("Product not found")));
    }

    @Override
    public Product getByProductId(Long id) {
        return productRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("Product not found"));
    }

    @Override
    public List<ProductResponse> getByCategory(ProductCategory category) {
        return productRepository.findByCategory(category).stream()
                .map(this::productToProductResponse)
                .toList();
    }

    @Override
    public void update(Long id, ProductRequest request) {
        getById(id);
        var product = this.productRequestToProduct(request);
        product.setId(id);
        Product saved = productRepository.save(product);
        genericProducer.send(new EventMessage(USER_UPDATED, this.productToProductResponse(saved)));
    }

    private ProductResponse productToProductResponse(Product saved) {
        return new ProductResponse(
                saved.getId(),
                saved.getName(),
                saved.getPrice(),
                saved.getCategory(),
                saved.getCreatedAt(),
                saved.getUpdatedAt());
    }

    private Product productRequestToProduct(ProductRequest request) {
        return new Product(null, request.name(), request.price(), request.category());
    }
}
