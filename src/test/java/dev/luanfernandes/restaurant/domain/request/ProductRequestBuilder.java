package dev.luanfernandes.restaurant.domain.request;

import static dev.luanfernandes.restaurant.common.constants.TestConstants.CATEGORY;
import static dev.luanfernandes.restaurant.common.constants.TestConstants.NAME;
import static dev.luanfernandes.restaurant.common.constants.TestConstants.PRICE;

public class ProductRequestBuilder {
    public static ProductRequest getProductRequest() {
        return new ProductRequest(NAME, PRICE, CATEGORY);
    }
}
