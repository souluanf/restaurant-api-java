package dev.luanfernandes.restaurant.messaging;

import static dev.luanfernandes.restaurant.domain.event.EventMessageBuilder.getEventMessage;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.slf4j.Logger;
import org.springframework.test.context.junit.jupiter.SpringExtension;

@ExtendWith(SpringExtension.class)
@DisplayName("Tests for GenericConsumer")
class GenericConsumerTest {

    @Mock
    private Logger log;

    @InjectMocks
    private GenericConsumer consumer;

    @Test
    void testConsume() {
        var testMessage = getEventMessage();
        consumer.consume(testMessage);
        verify(log, times(1)).info("Consuming message from RabbitMQ: {}", testMessage);
    }
}
