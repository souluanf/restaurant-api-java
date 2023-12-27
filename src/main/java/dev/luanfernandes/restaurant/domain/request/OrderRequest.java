package dev.luanfernandes.restaurant.domain.request;

import jakarta.validation.constraints.NotEmpty;
import java.util.Map;
import org.springframework.validation.annotation.Validated;

@Validated
public record OrderRequest(@NotEmpty Map<Long, Integer> products) {}
