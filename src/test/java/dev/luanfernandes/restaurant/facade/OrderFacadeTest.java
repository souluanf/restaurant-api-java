package dev.luanfernandes.restaurant.facade;

import static dev.luanfernandes.restaurant.common.constants.TestConstants.ID;
import static dev.luanfernandes.restaurant.domain.entity.OrderBuilder.getOrder;
import static dev.luanfernandes.restaurant.domain.entity.OrderBuilder.getOrderList;
import static dev.luanfernandes.restaurant.domain.entity.ProductBuilder.getProduct;
import static dev.luanfernandes.restaurant.domain.enums.EventType.ORDER_UPDATED;
import static dev.luanfernandes.restaurant.domain.enums.OrderStatus.NEW;
import static java.math.BigDecimal.valueOf;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertInstanceOf;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import dev.luanfernandes.restaurant.domain.entity.Order;
import dev.luanfernandes.restaurant.domain.event.EventMessage;
import dev.luanfernandes.restaurant.domain.request.OrderRequest;
import dev.luanfernandes.restaurant.domain.request.OrderUpdateQuantityRequest;
import dev.luanfernandes.restaurant.domain.response.OrderResponse;
import dev.luanfernandes.restaurant.messaging.GenericProducer;
import dev.luanfernandes.restaurant.service.OrderService;
import dev.luanfernandes.restaurant.service.ProductService;
import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
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

    @Captor
    private ArgumentCaptor<Order> orderCaptor;

    @Captor
    private ArgumentCaptor<EventMessage> eventMessageCaptor;

    @Test
    @DisplayName("Should return all orders")
    void shouldReturnAllOrders() {
        var product = getProduct();
        var orders = getOrderList();
        when(orderService.getAll()).thenReturn(orders);
        when(productService.getByProductId(anyLong())).thenReturn(product);
        var foundOrders = orderFacade.getAll();
        assertEquals(orders.size(), foundOrders.size(), "The number of orders should match");
    }

    @Test
    @DisplayName("Should create an order and send ORDER_CREATED event")
    void shouldCreateOrderAndSendEvent() {
        var product = getProduct();
        var productQuantities = Map.of(ID, 2);
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
        var product = getProduct();
        var order = getOrder();
        when(orderService.getById(ID)).thenReturn(order);
        when(productService.getByProductId(anyLong())).thenReturn(product);
        var foundOrder = orderFacade.getById(ID);
        assertNotNull(foundOrder);
        assertEquals(ID, foundOrder.id());
        assertEquals(order.getTotalValue(), foundOrder.totalValue());
    }

    @Test
    @DisplayName("Should update product quantity by adding and send ORDER_UPDATED event")
    void shouldUpdateByAddingProductQuantityAndSendEvent() {
        var product = getProduct();
        int additionalQuantity = 2;
        var updateRequest = new OrderUpdateQuantityRequest(ID, additionalQuantity);
        var newTotal = product.getPrice().multiply(valueOf(additionalQuantity + 1));
        var productsBeforeUpdate = new HashMap<Long, Integer>();
        productsBeforeUpdate.put(ID, 1);
        var orderBeforeUpdate = new Order(ID, productsBeforeUpdate, NEW, product.getPrice());
        when(orderService.getById(ID)).thenReturn(orderBeforeUpdate);
        Map<Long, Integer> productsAfterUpdate = new HashMap<>();
        productsAfterUpdate.put(ID, 1 + additionalQuantity);
        var updatedOrder = new Order(ID, productsAfterUpdate, NEW, newTotal);
        when(orderService.update(any(Order.class))).thenReturn(updatedOrder);
        when(productService.getByProductId(ID)).thenReturn(product);
        orderFacade.updateProductQuantityInOrder(ID, updateRequest);
        verify(orderService).update(orderCaptor.capture());
        var capturedOrder = orderCaptor.getValue();
        assertEquals(1 + additionalQuantity, capturedOrder.getProducts().get(ID));
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
        var product = getProduct();
        var removalQuantity = -1;
        var updateRequest = new OrderUpdateQuantityRequest(ID, removalQuantity);
        var productsBeforeRemoval = new HashMap<Long, Integer>();
        productsBeforeRemoval.put(ID, 1);
        var orderBeforeRemoval = new Order(ID, productsBeforeRemoval, NEW, product.getPrice());
        when(orderService.getById(ID)).thenReturn(orderBeforeRemoval);
        Map<Long, Integer> productsAfterRemoval = new HashMap<>();
        var updatedOrder = new Order(ID, productsAfterRemoval, NEW, BigDecimal.ZERO);
        when(orderService.update(any(Order.class))).thenReturn(updatedOrder);
        when(productService.getByProductId(ID)).thenReturn(product);
        orderFacade.updateProductQuantityInOrder(ID, updateRequest);
        verify(orderService).update(orderCaptor.capture());
        var capturedOrder = orderCaptor.getValue();
        assertFalse(capturedOrder.getProducts().containsKey(ID));
        verify(genericProducer).send(eventMessageCaptor.capture());
        var capturedEventMessage = eventMessageCaptor.getValue();
        assertEquals(ORDER_UPDATED, capturedEventMessage.eventType());
        assertInstanceOf(OrderResponse.class, capturedEventMessage.data());
    }
}
