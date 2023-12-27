package dev.luanfernandes.restaurant.domain.request;

import static dev.luanfernandes.restaurant.common.constants.TestConstants.ID;
import static dev.luanfernandes.restaurant.common.constants.TestConstants.QUANTITY;

import lombok.experimental.UtilityClass;

@UtilityClass
public class OrderUpdateQuantityRequestBuilder {
    public static OrderUpdateQuantityRequest getOrderUpdateQuantityRequest() {
        return new OrderUpdateQuantityRequest(ID, QUANTITY);
    }
}
