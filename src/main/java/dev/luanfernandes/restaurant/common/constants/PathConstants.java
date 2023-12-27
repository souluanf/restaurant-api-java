package dev.luanfernandes.restaurant.common.constants;

import lombok.experimental.UtilityClass;

@UtilityClass
public class PathConstants {
    private static final String API = "/api";

    public static final String AUTH_V1 = API + "/v1/auth";
    public static final String AUTH_TOKEN_V1 = AUTH_V1 + "/token";

    public static final String PRODUCTS_V1 = API + "/v1/products";
    public static final String PRODUCTS_BY_ID_V1 = API + "/v1/products/{id}";
    public static final String PRODUCTS_BY_CATEGORY_V1 = API + "/v1/products/category/{category}";
    public static final String ORDERS_V1 = API + "/v1/orders";
    public static final String ORDERS_BY_ID_V1 = API + "/v1/orders/{id}";
    public static final String PUBLISHER_V1 = API + "/v1/publisher";
}
