package dev.luanfernandes.restaurant.domain.response;

import static dev.luanfernandes.restaurant.common.constants.TestConstants.CATEGORY;
import static dev.luanfernandes.restaurant.common.constants.TestConstants.DATE;
import static dev.luanfernandes.restaurant.common.constants.TestConstants.ID;
import static dev.luanfernandes.restaurant.common.constants.TestConstants.NAME;
import static dev.luanfernandes.restaurant.common.constants.TestConstants.PRICE;
import static java.util.List.of;

import java.util.List;

public class ProductResponseBuilder {

    public static ProductResponse getProductResponse() {
        return new ProductResponse(ID, NAME, PRICE, CATEGORY, DATE, DATE);
    }

    public static List<ProductResponse> getProductResponseList() {
        return of(getProductResponse());
    }
}
