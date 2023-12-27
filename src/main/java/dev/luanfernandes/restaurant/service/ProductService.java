package dev.luanfernandes.restaurant.service;

import dev.luanfernandes.restaurant.domain.entity.Product;
import dev.luanfernandes.restaurant.domain.enums.ProductCategory;
import dev.luanfernandes.restaurant.domain.request.ProductRequest;
import dev.luanfernandes.restaurant.domain.response.ProductResponse;
import java.util.List;

public interface ProductService {
    ProductResponse create(ProductRequest request);

    List<ProductResponse> getAll();

    ProductResponse getById(Long id);

    Product getByProductId(Long id);

    List<ProductResponse> getByCategory(ProductCategory category);

    void update(Long id, ProductRequest request);
}
