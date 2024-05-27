package com.egt.gateway.service.rabbit;


import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class RabbitServiceImpl implements RabbitService {
    private static final String RATES_UPDATE = "RATES_UPDATE";

    private final RabbitTemplate rabbitTemplate;
    private final String exchange;
    private final String routingKey;


    public RabbitServiceImpl(RabbitTemplate rabbitTemplate,
                             @Value("${rabbitmq.exchange.name}") String exchange,
                             @Value("${rabbitmq.routing.key}") String routingKey) {
        this.rabbitTemplate = rabbitTemplate;
        this.exchange = exchange;
        this.routingKey = routingKey;
    }

    @Override
    public void sendMessageToWebSocketClient() {
        log.info("RabbitMQ message sent to {}.", exchange);
        rabbitTemplate.convertAndSend(exchange,
                routingKey, RATES_UPDATE);
    }
}
