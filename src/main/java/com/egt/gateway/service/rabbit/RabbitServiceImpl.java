package com.egt.gateway.service.rabbit;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

@Service
public class RabbitServiceImpl implements RabbitService {
    private static final String RATES_UPDATE = "RATES_UPDATE";


    private final SimpMessagingTemplate simpMessagingTemplate;
    private final String exchangeName;


    public RabbitServiceImpl(SimpMessagingTemplate simpMessagingTemplate,
                             @Value("${spring.rabbitmq.template.exchange}") final String exchangeName) {
        this.simpMessagingTemplate = simpMessagingTemplate;
        this.exchangeName = exchangeName;
    }

    @Override
    public void sendMessageToWebSocketClient() {
        simpMessagingTemplate.convertAndSend(exchangeName, RATES_UPDATE);
    }
}
