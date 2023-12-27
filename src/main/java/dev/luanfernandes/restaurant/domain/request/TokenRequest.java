package dev.luanfernandes.restaurant.domain.request;

import jakarta.validation.constraints.NotBlank;
import org.springframework.validation.annotation.Validated;

@Validated
public record TokenRequest(@NotBlank String username, @NotBlank String password) {}
