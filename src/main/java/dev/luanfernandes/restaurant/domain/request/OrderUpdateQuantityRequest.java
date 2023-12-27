package dev.luanfernandes.restaurant.domain.request;

import jakarta.validation.constraints.NotNull;
import org.springframework.validation.annotation.Validated;

@Validated
public record OrderUpdateQuantityRequest(@NotNull Long productId, @NotNull Integer quantity) {}
