package dev.luanfernandes.restaurant.domain.entity;

import static dev.luanfernandes.restaurant.common.constants.TestConstants.CATEGORY;
import static dev.luanfernandes.restaurant.common.constants.TestConstants.ID;
import static dev.luanfernandes.restaurant.common.constants.TestConstants.NAME;
import static dev.luanfernandes.restaurant.common.constants.TestConstants.PRICE;
import static java.util.List.of;

import java.util.List;
import lombok.experimental.UtilityClass;

@UtilityClass
public class ProductBuilder {
    public static Product getProduct() {
        return new Product(ID, NAME, PRICE, CATEGORY);
    }

    public static List<Product> getProductList() {
        return of(getProduct());
    }
}
