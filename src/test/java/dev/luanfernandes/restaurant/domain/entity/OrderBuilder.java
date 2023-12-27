package dev.luanfernandes.restaurant.domain.entity;

import static dev.luanfernandes.restaurant.common.constants.TestConstants.ID;
import static dev.luanfernandes.restaurant.common.constants.TestConstants.PRICE;
import static dev.luanfernandes.restaurant.common.constants.TestConstants.QUANTITY;
import static dev.luanfernandes.restaurant.domain.enums.OrderStatus.CONFIRMED;
import static java.util.List.of;
import static java.util.Map.of;

import java.util.List;
import java.util.Map;
import lombok.experimental.UtilityClass;

@UtilityClass
public class OrderBuilder {

    public static Order getOrder() {
        return new Order(ID, Map.of(ID, QUANTITY), CONFIRMED, PRICE);
    }

    public static List<Order> getOrderList() {
        return of(getOrder());
    }
}
