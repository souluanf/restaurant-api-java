package dev.luanfernandes.restaurant.service;

import static dev.luanfernandes.restaurant.common.constants.TestConstants.ID;
import static dev.luanfernandes.restaurant.domain.entity.OrderBuilder.getOrder;
import static dev.luanfernandes.restaurant.domain.entity.OrderBuilder.getOrderList;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import dev.luanfernandes.restaurant.domain.entity.Order;
import dev.luanfernandes.restaurant.repository.OrderRepository;
import dev.luanfernandes.restaurant.service.impl.OrderServiceImpl;
import jakarta.persistence.EntityNotFoundException;
import java.util.Optional;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
@DisplayName("Tests for OrderService")
class OrderServiceTest {

    @InjectMocks
    private OrderServiceImpl orderService;

    @Mock
    private OrderRepository orderRepository;

    @Test
    @DisplayName("Should save and return the order when created")
    void shouldSaveAndReturnOrderWhenCreated() {
        var order = getOrder();
        when(orderRepository.save(any(Order.class))).thenReturn(order);
        Order savedOrder = orderService.create(order);
        assertNotNull(savedOrder);
        verify(orderRepository).save(order);
    }

    @Test
    @DisplayName("Should return all orders")
    void shouldReturnAllOrders() {
        var orders = getOrderList();
        when(orderRepository.findAll()).thenReturn(orders);
        var foundOrders = orderService.getAll();
        assertEquals(orders.size(), foundOrders.size());
    }

    @Test
    @DisplayName("Should return the order when order with given ID exists")
    void shouldReturnOrderWhenExists() {
        var order = getOrder();
        when(orderRepository.findById(ID)).thenReturn(Optional.of(order));
        var foundOrder = orderService.getById(ID);
        assertNotNull(foundOrder);
        assertEquals(ID, foundOrder.getId());
    }

    @Test
    @DisplayName("Should throw EntityNotFoundException when order with given ID does not exist")
    void shouldThrowExceptionWhenOrderDoesNotExist() {
        when(orderRepository.findById(ID)).thenReturn(Optional.empty());
        assertThrows(EntityNotFoundException.class, () -> orderService.getById(ID));
    }

    @Test
    @DisplayName("Should update and return the order")
    void shouldUpdateAndReturnOrder() {
        var order = getOrder();
        when(orderRepository.findById(ID)).thenReturn(Optional.of(order));
        when(orderRepository.save(any(Order.class))).thenReturn(order);
        order.setId(ID);
        var updatedOrder = orderService.update(order);
        assertNotNull(updatedOrder);
        assertEquals(ID, updatedOrder.getId());
        verify(orderRepository).save(order);
    }
}
