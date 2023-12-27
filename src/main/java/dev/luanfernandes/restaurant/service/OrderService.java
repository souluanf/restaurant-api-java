package dev.luanfernandes.restaurant.service;

import dev.luanfernandes.restaurant.domain.entity.Order;
import java.util.List;

public interface OrderService {
    Order create(Order order);

    List<Order> getAll();

    Order getById(Long id);

    Order update(Order order);
}
