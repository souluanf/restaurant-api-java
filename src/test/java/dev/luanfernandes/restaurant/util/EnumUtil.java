package dev.luanfernandes.restaurant.util;

import static java.util.concurrent.ThreadLocalRandom.current;
import static org.apache.commons.lang3.EnumUtils.getEnumList;

public class EnumUtil {
    public static <E extends Enum<E>> E randomEnum(Class<E> enumClass) {
        var enumConstants = getEnumList(enumClass);
        var randomIndex = current().nextInt(0, enumConstants.size());
        return enumConstants.get(randomIndex);
    }
}
