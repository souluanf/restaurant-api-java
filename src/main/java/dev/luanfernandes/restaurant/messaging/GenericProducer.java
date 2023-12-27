package dev.luanfernandes.restaurant.messaging;

import dev.luanfernandes.restaurant.domain.event.EventMessage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class GenericProducer {

    private final RabbitTemplate rabbitTemplate;

    @Value("${spring.rabbitmq.exchange.name}")
    private String exchange;

    @Value("${spring.rabbitmq.routing.key}")
    private String routingKey;

    public void send(EventMessage eventMessage) {
        log.info("Sending message to RabbitMQ: {}", eventMessage.data());
        rabbitTemplate.convertAndSend(exchange, routingKey, eventMessage);
    }
}
