package com.example.demo.config;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.TopicExchange;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class NotificationRabbitMQConfig {
	
	@Value("${rabbitmq.queue.created_queue}")
	private String accountCreatedQueue;
	
	@Value("${rabbitmq.exchange.account_exchange}")
	private String accountExchange;
	
	@Value("${rabbitmq.routing.created_routingKey}")
	private String accountCreatedRoutingKey;
	
	@Value("${rabbitmq.queue.transaction.completed_queue}")
	private String transactionCompletedQueue;
	
	@Value("${rabbitmq.exchange.transaction_exchange}")
	private String transactionExchange;
	
	@Value("${rabbitmq.routing.transaction.completed_routingKey}")
	private String transactionCompletedRoutingKey;
	
	@Bean
	public Queue transactionCompletedQueue() {
		return new Queue(transactionCompletedQueue);
	}
	
	@Bean
	public TopicExchange transactionExchange() {
		return new TopicExchange(transactionExchange);
	}
	
	@Bean
	public Queue accountCreatedQueue() {
		return new Queue(accountCreatedQueue);
	}
	
	@Bean
	public TopicExchange accountExchange() {
		return new TopicExchange(accountExchange);
	}
	
	@Bean
	public Binding accountBinding() {
		return BindingBuilder.bind(accountCreatedQueue()).to(accountExchange()).with(accountCreatedRoutingKey);
	}
	
	@Bean
	public Binding transactionBinding() {
		return BindingBuilder.bind(transactionCompletedQueue()).to(transactionExchange()).with(transactionCompletedRoutingKey);
	}
	
	@Bean
    public MessageConverter jsonMessageConverter() {
        return new Jackson2JsonMessageConverter();
    }
	    
	    @Bean
	    public RabbitTemplate rabbitTemplate(ConnectionFactory connectionFactory) {
	        RabbitTemplate template = new RabbitTemplate(connectionFactory);
	        template.setMessageConverter(jsonMessageConverter());
	        return template;
	    }
}
