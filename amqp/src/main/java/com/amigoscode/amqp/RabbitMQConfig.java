package com.amigoscode.amqp;

import lombok.AllArgsConstructor;
import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.amqp.rabbit.config.SimpleRabbitListenerContainerFactory;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@AllArgsConstructor
public class RabbitMQConfig {


    private final ConnectionFactory connectionFactory;

    /*
    * This is the Rabbit MQ Template to be used by the publisher to send messages
    */
    @Bean
    public AmqpTemplate amqpTemplate(){
        RabbitTemplate rabbitTemplate = new RabbitTemplate(connectionFactory);
        rabbitTemplate.setMessageConverter(jacksonConverter());
        return rabbitTemplate;
    }

    /*
    * This is the listener to be used by the consumer to retrieve messages
    */
    @Bean
    public SimpleRabbitListenerContainerFactory simpleRabbitListenerContainerFactory(){
        SimpleRabbitListenerContainerFactory factory = new SimpleRabbitListenerContainerFactory();
        factory.setConnectionFactory(connectionFactory);
        factory.setMessageConverter(jacksonConverter());
        return factory;
    }


    /*
    * This Bean is injected to the AmqpTemplate and the SimpleRabbitListenerContainerFactory
    * This is to convert the publisher message to json before publishing
    * And the consumer can deserialize as it needs
    */
    @Bean
    public MessageConverter jacksonConverter(){
        MessageConverter jackson2JsonConverter = new Jackson2JsonMessageConverter();
        return jackson2JsonConverter;
    }
}
