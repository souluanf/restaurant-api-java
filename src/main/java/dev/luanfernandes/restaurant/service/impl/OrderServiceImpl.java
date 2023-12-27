package dev.luanfernandes.restaurant.service.impl;

import dev.luanfernandes.restaurant.domain.entity.Order;
import dev.luanfernandes.restaurant.repository.OrderRepository;
import dev.luanfernandes.restaurant.service.OrderService;
import jakarta.persistence.EntityNotFoundException;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class OrderServiceImpl implements OrderService {

    private final OrderRepository orderRepository;

    @Override
    public Order create(Order order) {
        return orderRepository.save(order);
    }

    @Override
    public List<Order> getAll() {
        return orderRepository.findAll();
    }

    @Override
    public Order getById(Long id) {
        return orderRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("Order not found: " + id));
    }

    @Override
    public Order update(Order order) {
        getById(order.getId());
        return orderRepository.save(order);
    }
}
