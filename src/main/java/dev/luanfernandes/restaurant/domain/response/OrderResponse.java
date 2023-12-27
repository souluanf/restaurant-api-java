package dev.luanfernandes.restaurant.domain.response;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

public record OrderResponse(
        Long id,
        List<ProductInfoResponse> products,
        BigDecimal totalValue,
        LocalDateTime createdAt,
        LocalDateTime updatedAt) {}
