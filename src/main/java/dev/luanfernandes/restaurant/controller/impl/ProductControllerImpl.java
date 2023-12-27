package dev.luanfernandes.restaurant.controller.impl;

import static org.springframework.http.HttpStatus.CREATED;
import static org.springframework.http.ResponseEntity.noContent;
import static org.springframework.http.ResponseEntity.ok;
import static org.springframework.http.ResponseEntity.status;

import dev.luanfernandes.restaurant.controller.ProductController;
import dev.luanfernandes.restaurant.domain.enums.ProductCategory;
import dev.luanfernandes.restaurant.domain.request.ProductRequest;
import dev.luanfernandes.restaurant.domain.response.ProductResponse;
import dev.luanfernandes.restaurant.service.ProductService;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class ProductControllerImpl implements ProductController {

    private final ProductService productService;

    @Override
    public ResponseEntity<ProductResponse> create(ProductRequest request) {
        return status(CREATED).body(productService.create(request));
    }

    @Override
    public ResponseEntity<List<ProductResponse>> getAll() {
        return ok(productService.getAll());
    }

    @Override
    public ResponseEntity<ProductResponse> getById(Long id) {
        return ok(productService.getById(id));
    }

    @Override
    public ResponseEntity<List<ProductResponse>> getByCategory(ProductCategory category) {
        return ok(productService.getByCategory(category));
    }

    @Override
    public ResponseEntity<Object> update(Long id, ProductRequest request) {
        productService.update(id, request);
        return noContent().build();
    }
}
