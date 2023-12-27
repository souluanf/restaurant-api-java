package dev.luanfernandes.restaurant.domain.response;

import static dev.luanfernandes.restaurant.common.constants.TestConstants.DATE;
import static dev.luanfernandes.restaurant.common.constants.TestConstants.ID;
import static dev.luanfernandes.restaurant.common.constants.TestConstants.PRICE;
import static dev.luanfernandes.restaurant.domain.response.ProductInfoResponseBuilder.getProductInfoResponseList;
import static java.util.List.of;

import java.util.List;

public class OrderResponseBuilder {

    public static OrderResponse getOrderResponse() {
        return new OrderResponse(ID, getProductInfoResponseList(), PRICE, DATE, DATE);
    }

    public static List<OrderResponse> getOrderResponseList() {
        return of(getOrderResponse());
    }
}
