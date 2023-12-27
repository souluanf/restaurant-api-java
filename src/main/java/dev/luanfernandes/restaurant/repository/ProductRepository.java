package dev.luanfernandes.restaurant.repository;

import dev.luanfernandes.restaurant.domain.entity.Product;
import dev.luanfernandes.restaurant.domain.enums.ProductCategory;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {
    List<Product> findByCategory(ProductCategory category);
}
