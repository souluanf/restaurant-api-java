package dev.luanfernandes.restaurant.domain.response;

import dev.luanfernandes.restaurant.domain.enums.ProductCategory;
import java.math.BigDecimal;
import java.time.LocalDateTime;

public record ProductResponse(
        Long id,
        String name,
        BigDecimal price,
        ProductCategory category,
        LocalDateTime createdAt,
        LocalDateTime updatedAt) {}
