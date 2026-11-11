package com.exemple.user.messaging;

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
    public static final String REQUEST_QUEUE = "user.premium.activation.requested.q";
    public static final String REQUEST_KEY = "subscription.premium.activation.requested";
    public static final String VALIDATED_KEY = "user.premium.activation.validated";
    public static final String REJECTED_KEY = "user.premium.activation.rejected";

    @Bean
    TopicExchange premiumEventsExchange() {
        return new TopicExchange(EXCHANGE, true, false);
    }

    @Bean
    Queue premiumActivationRequestQueue() {
        return QueueBuilder.durable(REQUEST_QUEUE).build();
    }

    @Bean
    Binding premiumActivationRequestBinding(Queue premiumActivationRequestQueue,
                                             TopicExchange premiumEventsExchange) {
        return BindingBuilder.bind(premiumActivationRequestQueue)
                .to(premiumEventsExchange).with(REQUEST_KEY);
    }
}