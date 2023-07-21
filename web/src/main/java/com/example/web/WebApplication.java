package com.example.web;

import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.amqp.rabbit.config.SimpleRabbitListenerContainerFactory;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@ComponentScan("com.example")
public class WebApplication {

    @Value("${messaging.consumer.initial-size}")
    private int CONSUMER_SIZE;

    @Value("${messaging.consumer.result.auto-start}")
    private boolean CONSUMER_RESULT_AUTO_START;

    @Value("${messaging.consumer.result.max-size}")
    private int CONSUMER_RESULT_MAX_SIZE;

    @Value("${messaging.consumer.interval}")
    private Long INTERVAL_IN_MS;


    @Value("${messaging.queue.coordinate.result.problem}")
    private String MESSAGING_COORDINATE_RESULT_PROBLEM_QUEUE;

    @Value("${messaging.queue.coordinate.request}")
    private String MESSAGING_COORDINATE_REQUEST_QUEUE;

    @Value("${messaging.queue.coordinate.result")
    private String MESSAGING_COORDINATE_RESULT_QUEUE;


    @Value("${messaging.queue.city.result.problem}")
    private String MESSAGING_CITY_RESULT_PROBLEM_QUEUE;


    @Value("${messaging.queue.city.request}")
    private String MESSAGING_CITY_REQUEST_QUEUE;

    @Value("${messaging.queue.city.result}")
    private String MESSAGING_CITY_RESULT_QUEUE;




    @Bean
    public AmqpTemplate coordinateResultProblemQueueTemplate(ConnectionFactory rabbitConnectionFactory,
                                                             MessageConverter messageConverter)
    {
        RabbitTemplate template = new RabbitTemplate(rabbitConnectionFactory);
        template.setRoutingKey(MESSAGING_COORDINATE_RESULT_PROBLEM_QUEUE);
        template.setMessageConverter(messageConverter);
        return template;
    }


    @Bean
    public AmqpTemplate cityResultProblemQueueTemplate(ConnectionFactory rabbitConnectionFactory,
                                                       MessageConverter messageConverter)
    {
        RabbitTemplate template = new RabbitTemplate(rabbitConnectionFactory);
        template.setRoutingKey(MESSAGING_CITY_RESULT_PROBLEM_QUEUE);
        template.setMessageConverter(messageConverter);
        return template;
    }



    @Bean
    public AmqpTemplate coordinateRequestQueueTemplate(ConnectionFactory rabbitConnectionFactory,
                                                       MessageConverter messageConverter)
    {
        RabbitTemplate template = new RabbitTemplate(rabbitConnectionFactory);
        template.setRoutingKey(MESSAGING_COORDINATE_REQUEST_QUEUE);
        template.setMessageConverter(messageConverter);
        return template;
    }


    @Bean
    public AmqpTemplate cityRequestQueueTemplate(ConnectionFactory rabbitConnectionFactory,
                                                 MessageConverter messageConverter)
    {
        RabbitTemplate template = new RabbitTemplate(rabbitConnectionFactory);
        template.setRoutingKey(MESSAGING_CITY_REQUEST_QUEUE);
        template.setMessageConverter(messageConverter);
        return template;
    }

    @Bean
    public AmqpTemplate coordinateResultQueueTemplate(ConnectionFactory rabbitConnectionFactory,
                                                      MessageConverter messageConverter)
    {
        RabbitTemplate template = new RabbitTemplate(rabbitConnectionFactory);
        template.setRoutingKey(MESSAGING_COORDINATE_RESULT_QUEUE);
        template.setMessageConverter(messageConverter);
        return template;
    }

    @Bean
    public AmqpTemplate cityResultQueueTemplate(ConnectionFactory rabbitConnectionFactory,
                                                MessageConverter messageConverter)
    {
        RabbitTemplate template = new RabbitTemplate(rabbitConnectionFactory);
        template.setRoutingKey(MESSAGING_CITY_RESULT_QUEUE);
        template.setMessageConverter(messageConverter);
        return template;
    }

    @Bean
    public SimpleRabbitListenerContainerFactory resultQueueListener(ConnectionFactory connectionFactory,
                                                                    MessageConverter messageConverter)
    {
        SimpleRabbitListenerContainerFactory container = new SimpleRabbitListenerContainerFactory();
        container.setConnectionFactory(connectionFactory);
        container.setMessageConverter(messageConverter);
        container.setConcurrentConsumers(CONSUMER_SIZE);
        container.setStartConsumerMinInterval(INTERVAL_IN_MS);
        container.setStopConsumerMinInterval(INTERVAL_IN_MS);
        container.setPrefetchCount(10);
        container.setMaxConcurrentConsumers(CONSUMER_RESULT_MAX_SIZE);
        container.setAutoStartup(CONSUMER_RESULT_AUTO_START);
        return container;
    }

    public static void main(String[] args) {
        SpringApplication.run(WebApplication.class, args);
    }

}
