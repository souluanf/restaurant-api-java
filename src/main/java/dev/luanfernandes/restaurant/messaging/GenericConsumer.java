package dev.luanfernandes.restaurant.messaging;

import dev.luanfernandes.restaurant.domain.event.EventMessage;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class GenericConsumer {

    private final Logger log;

    @RabbitListener(queues = "${spring.rabbitmq.queue.name}")
    public void consume(EventMessage eventMessage) {
        log.info("Consuming message from RabbitMQ: {}", eventMessage);
    }
}
