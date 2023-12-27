package dev.luanfernandes.restaurant.common.constants;

import static dev.luanfernandes.restaurant.util.EnumUtil.randomEnum;
import static java.time.LocalDateTime.now;
import static java.util.concurrent.ThreadLocalRandom.current;

import dev.luanfernandes.restaurant.domain.enums.ProductCategory;
import java.math.BigDecimal;
import java.time.LocalDateTime;

public class TestConstants {
    public static final String NAME = "name";
    public static final String ANY_STRING = "any_string";
    public static final String TOKEN = "TOKEN";

    public static final Long ID = current().nextLong();
    public static final Integer QUANTITY = current().nextInt(1, 100);
    public static final BigDecimal PRICE = new BigDecimal(current().nextInt(1, 1000));
    public static final LocalDateTime DATE = now();
    public static final ProductCategory CATEGORY = randomEnum(ProductCategory.class);
}
