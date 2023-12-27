package dev.luanfernandes.restaurant.domain.event;

import static dev.luanfernandes.restaurant.common.constants.TestConstants.ANY_STRING;
import static dev.luanfernandes.restaurant.domain.enums.EventType.USER_CREATED;

import lombok.experimental.UtilityClass;

@UtilityClass
public class EventMessageBuilder {

    public static EventMessage getEventMessage() {
        return new EventMessage(USER_CREATED, ANY_STRING);
    }
}
