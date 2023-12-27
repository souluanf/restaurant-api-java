package dev.luanfernandes.restaurant.domain.response;

import java.math.BigDecimal;

public record ProductInfoResponse(
        Long productId, String productName, Integer quantity, BigDecimal unitPrice, BigDecimal totalPrice) {}
