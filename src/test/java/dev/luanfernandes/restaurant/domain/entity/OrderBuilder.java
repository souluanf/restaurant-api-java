package dev.luanfernandes.restaurant.domain.entity;

import static dev.luanfernandes.restaurant.common.constants.TestConstants.ID;
import static dev.luanfernandes.restaurant.domain.entity.ProductBuilder.getProduct;
import static dev.luanfernandes.restaurant.domain.enums.OrderStatus.NEW;
import static java.math.BigDecimal.valueOf;
import static java.util.List.of;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import lombok.experimental.UtilityClass;

@UtilityClass
public class OrderBuilder {

    public static Order getOrder() {
        Product product = getProduct();
        Map<Long, Integer> productQuantities = new HashMap<>();
        productQuantities.put(ID, 1);
        return new Order(ID, productQuantities, NEW, product.getPrice().multiply(valueOf(1)));
    }

    public static List<Order> getOrderList() {
        return of(getOrder());
    }
}
