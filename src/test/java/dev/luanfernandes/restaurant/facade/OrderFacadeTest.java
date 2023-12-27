package dev.luanfernandes.restaurant.facade;

import static dev.luanfernandes.restaurant.domain.enums.EventType.ORDER_UPDATED;
import static dev.luanfernandes.restaurant.domain.enums.OrderStatus.NEW;
import static dev.luanfernandes.restaurant.domain.enums.ProductCategory.PRATO_PRINCIPAL;
import static java.math.BigDecimal.valueOf;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import dev.luanfernandes.restaurant.domain.entity.Order;
import dev.luanfernandes.restaurant.domain.entity.Product;
import dev.luanfernandes.restaurant.domain.event.EventMessage;
import dev.luanfernandes.restaurant.domain.request.OrderRequest;
import dev.luanfernandes.restaurant.domain.request.OrderUpdateQuantityRequest;
import dev.luanfernandes.restaurant.domain.response.OrderResponse;
import dev.luanfernandes.restaurant.messaging.GenericProducer;
import dev.luanfernandes.restaurant.service.OrderService;
import dev.luanfernandes.restaurant.service.ProductService;
import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
@DisplayName("Tests for OrderFacade")
class OrderFacadeTest {

    @InjectMocks
    private OrderFacade orderFacade;

    @Mock
    private OrderService orderService;

    @Mock
    private ProductService productService;

    @Mock
    private GenericProducer genericProducer;

    private ArgumentCaptor<Order> orderCaptor;
    private ArgumentCaptor<EventMessage> eventMessageCaptor;

    private Order order;
    private Product product;

    private final Long productId = 1L;
    private final Long orderId = 1L;

    @BeforeEach
    void setUp() {
        product = new Product(productId, "Product Name", new BigDecimal("20.00"), PRATO_PRINCIPAL);
        Map<Long, Integer> productQuantities = new HashMap<>();
        productQuantities.put(productId, 1);
        order = new Order(orderId, productQuantities, NEW, product.getPrice().multiply(valueOf(1)));
        orderCaptor = ArgumentCaptor.forClass(Order.class);
        eventMessageCaptor = ArgumentCaptor.forClass(EventMessage.class);
    }

    @Test
    @DisplayName("Should return all orders")
    void shouldReturnAllOrders() {
        var orders = List.of(order);
        when(orderService.getAll()).thenReturn(orders);
        when(productService.getByProductId(anyLong())).thenReturn(product);
        var foundOrders = orderFacade.getAll();
        assertEquals(orders.size(), foundOrders.size(), "The number of orders should match");
    }

    @Test
    @DisplayName("Should create an order and send ORDER_CREATED event")
    void shouldCreateOrderAndSendEvent() {
        var productQuantities = Map.of(productId, 2);
        var newOrder =
                new Order(null, productQuantities, NEW, product.getPrice().multiply(valueOf(2)));
        when(productService.getByProductId(anyLong())).thenReturn(product);
        when(orderService.create(any(Order.class))).thenReturn(newOrder);
        var createdOrder = orderFacade.create(new OrderRequest(productQuantities));
        assertNotNull(createdOrder);
        assertEquals(newOrder.getTotalValue(), createdOrder.totalValue());
    }

    @Test
    @DisplayName("Should return the order when order with given ID exists")
    void shouldReturnOrderWhenExists() {
        when(orderService.getById(orderId)).thenReturn(order);
        when(productService.getByProductId(anyLong())).thenReturn(product);
        var foundOrder = orderFacade.getById(orderId);
        assertNotNull(foundOrder);
        assertEquals(orderId, foundOrder.id());
        assertEquals(order.getTotalValue(), foundOrder.totalValue());
    }

    @Test
    @DisplayName("Should update product quantity by adding and send ORDER_UPDATED event")
    void shouldUpdateByAddingProductQuantityAndSendEvent() {
        int additionalQuantity = 2;
        var updateRequest = new OrderUpdateQuantityRequest(productId, additionalQuantity);
        var newTotal = product.getPrice().multiply(valueOf(additionalQuantity + 1));
        var productsBeforeUpdate = new HashMap<Long, Integer>();
        productsBeforeUpdate.put(productId, 1);
        var orderBeforeUpdate = new Order(orderId, productsBeforeUpdate, NEW, product.getPrice());
        when(orderService.getById(orderId)).thenReturn(orderBeforeUpdate);
        Map<Long, Integer> productsAfterUpdate = new HashMap<>();
        productsAfterUpdate.put(productId, 1 + additionalQuantity);
        var updatedOrder = new Order(orderId, productsAfterUpdate, NEW, newTotal);
        when(orderService.update(any(Order.class))).thenReturn(updatedOrder);
        when(productService.getByProductId(productId)).thenReturn(product);
        orderFacade.updateProductQuantityInOrder(orderId, updateRequest);
        verify(orderService).update(orderCaptor.capture());
        var capturedOrder = orderCaptor.getValue();
        assertEquals(1 + additionalQuantity, capturedOrder.getProducts().get(productId));
        assertEquals(newTotal, capturedOrder.getTotalValue());
        verify(genericProducer).send(eventMessageCaptor.capture());
        var capturedEventMessage = eventMessageCaptor.getValue();
        assertEquals(ORDER_UPDATED, capturedEventMessage.eventType());
        assertNotNull(capturedEventMessage.data());
        assertInstanceOf(OrderResponse.class, capturedEventMessage.data());
    }

    @Test
    @DisplayName("Should remove product from order when updated quantity is zero or less and send ORDER_UPDATED event")
    void shouldRemoveProductWhenQuantityZeroOrLessAndSendEvent() {
        var removalQuantity = -1;
        var updateRequest = new OrderUpdateQuantityRequest(productId, removalQuantity);
        var productsBeforeRemoval = new HashMap<Long, Integer>();
        productsBeforeRemoval.put(productId, 1);
        var orderBeforeRemoval = new Order(orderId, productsBeforeRemoval, NEW, product.getPrice());
        when(orderService.getById(orderId)).thenReturn(orderBeforeRemoval);
        Map<Long, Integer> productsAfterRemoval = new HashMap<>();
        var updatedOrder = new Order(orderId, productsAfterRemoval, NEW, BigDecimal.ZERO);
        when(orderService.update(any(Order.class))).thenReturn(updatedOrder);
        when(productService.getByProductId(productId)).thenReturn(product);
        orderFacade.updateProductQuantityInOrder(orderId, updateRequest);
        verify(orderService).update(orderCaptor.capture());
        var capturedOrder = orderCaptor.getValue();
        assertFalse(capturedOrder.getProducts().containsKey(productId));
        verify(genericProducer).send(eventMessageCaptor.capture());
        var capturedEventMessage = eventMessageCaptor.getValue();
        assertEquals(ORDER_UPDATED, capturedEventMessage.eventType());
        assertInstanceOf(OrderResponse.class, capturedEventMessage.data());
    }
}
