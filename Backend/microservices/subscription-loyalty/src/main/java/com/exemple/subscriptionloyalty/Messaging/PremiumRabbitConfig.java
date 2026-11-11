package com.exemple.subscriptionloyalty.Messaging;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.QueueBuilder;
import org.springframework.amqp.core.TopicExchange;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.beans.factory.annotation.Qualifier;

@Configuration
public class PremiumRabbitConfig {

    public static final String EXCHANGE = "showmatch.events";
    public static final String VALIDATED_QUEUE = "subscription.premium.activation.validated.q";
    public static final String REJECTED_QUEUE = "subscription.premium.activation.rejected.q";
    public static final String VALIDATED_KEY = "user.premium.activation.validated";
    public static final String REJECTED_KEY = "user.premium.activation.rejected";
    public static final String REQUESTED_KEY = "subscription.premium.activation.requested";
    public static final String ACTIVATED_KEY = "subscription.premium.activated";

    @Bean
    TopicExchange premiumEventsExchange() {
        return new TopicExchange(EXCHANGE, true, false);
    }

    @Bean
    Queue premiumActivationValidatedQueue() {
        return QueueBuilder.durable(VALIDATED_QUEUE).build();
    }

    @Bean
    Queue premiumActivationRejectedQueue() {
        return QueueBuilder.durable(REJECTED_QUEUE).build();
    }

    @Bean
    Binding premiumActivationValidatedBinding(
                                               @Qualifier("premiumActivationValidatedQueue") Queue premiumActivationValidatedQueue,
                                               TopicExchange premiumEventsExchange) {
        return BindingBuilder.bind(premiumActivationValidatedQueue).to(premiumEventsExchange).with(VALIDATED_KEY);
    }

    @Bean
    Binding premiumActivationRejectedBinding(
                                              @Qualifier("premiumActivationRejectedQueue") Queue premiumActivationRejectedQueue,
                                              TopicExchange premiumEventsExchange) {
        return BindingBuilder.bind(premiumActivationRejectedQueue).to(premiumEventsExchange).with(REJECTED_KEY);
    }
}