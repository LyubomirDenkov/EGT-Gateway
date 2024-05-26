package com.egt.gateway.config;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.TopicExchange;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitConfiguration {

    @Bean
    public TopicExchange customTopicExchange(@Value("${spring.rabbitmq.template.exchange}") final String exchangeName) {
        return new TopicExchange(exchangeName, true, false);
    }

}
