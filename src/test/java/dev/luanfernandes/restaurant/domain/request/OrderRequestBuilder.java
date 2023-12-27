package dev.luanfernandes.restaurant.domain.request;

import static dev.luanfernandes.restaurant.common.constants.TestConstants.ID;
import static dev.luanfernandes.restaurant.common.constants.TestConstants.QUANTITY;
import static java.util.Collections.singletonMap;

public class OrderRequestBuilder {
    public static OrderRequest getOrderRequest() {
        return new OrderRequest(singletonMap(ID, QUANTITY));
    }
}
