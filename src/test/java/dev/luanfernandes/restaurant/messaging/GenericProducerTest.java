package dev.luanfernandes.restaurant.messaging;

import static dev.luanfernandes.restaurant.domain.event.EventMessageBuilder.getEventMessage;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.springframework.test.util.ReflectionTestUtils.setField;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.amqp.rabbit.core.RabbitTemplate;

@ExtendWith(MockitoExtension.class)
@DisplayName("Tests for GenericProducer")
class GenericProducerTest {

    @Mock
    private RabbitTemplate rabbitTemplate;

    @InjectMocks
    private GenericProducer producer;

    @Test
    void testSend() {
        var testExchange = "test.exchange";
        var testRoutingKey = "test.routing.key";
        var testMessage = getEventMessage();
        setField(producer, "exchange", testExchange);
        setField(producer, "routingKey", testRoutingKey);
        producer.send(testMessage);
        verify(rabbitTemplate, times(1)).convertAndSend(testExchange, testRoutingKey, testMessage);
    }
}
