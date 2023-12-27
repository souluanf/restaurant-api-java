package dev.luanfernandes.restaurant.domain.response;

import static dev.luanfernandes.restaurant.common.constants.TestConstants.ID;
import static dev.luanfernandes.restaurant.common.constants.TestConstants.NAME;
import static dev.luanfernandes.restaurant.common.constants.TestConstants.PRICE;
import static dev.luanfernandes.restaurant.common.constants.TestConstants.QUANTITY;
import static java.util.List.of;

import java.util.List;

public class ProductInfoResponseBuilder {

    public static ProductInfoResponse getProductInfoResponse() {
        return new ProductInfoResponse(ID, NAME, QUANTITY, PRICE, PRICE);
    }

    public static List<ProductInfoResponse> getProductInfoResponseList() {
        return of(getProductInfoResponse());
    }
}
