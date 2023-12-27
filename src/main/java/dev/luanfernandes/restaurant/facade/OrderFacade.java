package dev.luanfernandes.restaurant.facade;

import static dev.luanfernandes.restaurant.domain.enums.EventType.ORDER_CREATED;
import static dev.luanfernandes.restaurant.domain.enums.EventType.ORDER_UPDATED;
import static dev.luanfernandes.restaurant.domain.enums.OrderStatus.NEW;
import static java.math.BigDecimal.ZERO;
import static java.math.BigDecimal.valueOf;

import dev.luanfernandes.restaurant.domain.entity.Order;
import dev.luanfernandes.restaurant.domain.entity.Product;
import dev.luanfernandes.restaurant.domain.event.EventMessage;
import dev.luanfernandes.restaurant.domain.request.OrderRequest;
import dev.luanfernandes.restaurant.domain.request.OrderUpdateQuantityRequest;
import dev.luanfernandes.restaurant.domain.response.OrderResponse;
import dev.luanfernandes.restaurant.domain.response.ProductInfoResponse;
import dev.luanfernandes.restaurant.messaging.GenericProducer;
import dev.luanfernandes.restaurant.service.OrderService;
import dev.luanfernandes.restaurant.service.ProductService;
import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class OrderFacade {

    private final OrderService orderService;
    private final ProductService productService;
    private final GenericProducer genericProducer;

    public OrderResponse create(OrderRequest request) {
        Order order = new Order();
        order.setStatus(NEW);
        BigDecimal totalValue = ZERO;
        Map<Long, Integer> productQuantities = new HashMap<>();
        for (Map.Entry<Long, Integer> entry : request.products().entrySet()) {
            Product product = productService.getByProductId(entry.getKey());
            productQuantities.put(product.getId(), entry.getValue());
            totalValue = totalValue.add(product.getPrice().multiply(valueOf(entry.getValue())));
        }
        order.setProducts(productQuantities);
        order.setTotalValue(totalValue);
        OrderResponse orderResponse = convertToOrderResponse(orderService.create(order));
        genericProducer.send(new EventMessage(ORDER_CREATED, orderResponse));
        return orderResponse;
    }

    public List<OrderResponse> getAll() {
        return orderService.getAll().stream().map(this::convertToOrderResponse).toList();
    }

    public OrderResponse getById(Long id) {
        return convertToOrderResponse(orderService.getById(id));
    }

    public void updateProductQuantityInOrder(Long orderId, OrderUpdateQuantityRequest request) {
        Order order = orderService.getById(orderId);
        Product product = productService.getByProductId(request.productId());
        Map<Long, Integer> productQuantities = order.getProducts();
        Integer currentQuantity = productQuantities.getOrDefault(request.productId(), 0);
        int newQuantity = currentQuantity + request.quantity();
        if (newQuantity <= 0) {
            removeProductFromOrder(order, product, currentQuantity);
        } else {
            updateProductQuantity(order, product, newQuantity, request.quantity());
        }
        Order updated = orderService.update(order);
        genericProducer.send(new EventMessage(ORDER_UPDATED, convertToOrderResponse(updated)));
    }

    private void removeProductFromOrder(Order order, Product product, int currentQuantity) {
        order.getProducts().remove(product.getId());
        BigDecimal priceReduction = product.getPrice().multiply(valueOf(currentQuantity));
        order.setTotalValue(order.getTotalValue().subtract(priceReduction));
    }

    private void updateProductQuantity(Order order, Product product, int newQuantity, int additionalQuantity) {
        order.getProducts().put(product.getId(), newQuantity);
        BigDecimal priceIncrease = product.getPrice().multiply(valueOf(additionalQuantity));
        order.setTotalValue(order.getTotalValue().add(priceIncrease));
    }

    private OrderResponse convertToOrderResponse(Order order) {
        List<ProductInfoResponse> productInfoResponses = order.getProducts().entrySet().stream()
                .map(entry -> {
                    Long productId = entry.getKey();
                    Integer quantity = entry.getValue();
                    Product product = productService.getByProductId(productId);
                    BigDecimal totalPrice = product.getPrice().multiply(valueOf(quantity));
                    return new ProductInfoResponse(
                            productId, product.getName(), quantity, product.getPrice(), totalPrice);
                })
                .toList();

        return new OrderResponse(
                order.getId(), productInfoResponses, order.getTotalValue(), order.getCreatedAt(), order.getUpdatedAt());
    }
}
