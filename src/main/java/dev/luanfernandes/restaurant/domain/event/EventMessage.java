package dev.luanfernandes.restaurant.domain.event;

import dev.luanfernandes.restaurant.domain.enums.EventType;

public record EventMessage(EventType eventType, Object data) {}
