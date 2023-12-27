package dev.luanfernandes.restaurant.domain.request;

import dev.luanfernandes.restaurant.domain.enums.ProductCategory;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.math.BigDecimal;
import org.springframework.validation.annotation.Validated;

@Validated
public record ProductRequest(@NotBlank String name, @NotNull BigDecimal price, @NotNull ProductCategory category) {}
