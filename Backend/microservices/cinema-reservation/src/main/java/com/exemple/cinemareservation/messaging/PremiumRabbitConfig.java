package com.exemple.cinemareservation.messaging;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.QueueBuilder;
import org.springframework.amqp.core.TopicExchange;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class PremiumRabbitConfig {

    public static final String EXCHANGE = "showmatch.events";
    public static final String ACTIVATED_QUEUE = "cinema.premium.activated.q";
    public static final String ACTIVATED_KEY = "subscription.premium.activated";

    @Bean
    TopicExchange premiumEventsExchange() {
        return new TopicExchange(EXCHANGE, true, false);
    }

    @Bean
    Queue premiumActivatedQueue() {
        return QueueBuilder.durable(ACTIVATED_QUEUE).build();
    }

    @Bean
    Binding premiumActivatedBinding(Queue premiumActivatedQueue, TopicExchange premiumEventsExchange) {
        return BindingBuilder.bind(premiumActivatedQueue).to(premiumEventsExchange).with(ACTIVATED_KEY);
    }
}