package com.example.demo.producer;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.example.demo.dtos.TransactionEmailNotificationEvent;

@Service
public class TransactionEmailNotificationProducer {
	
	@Autowired
	private RabbitTemplate rabbitTemplate;
	
	@Value("${rabbitmq.exchange.transaction_exchange}")
	private String exchange;
	
	@Value("${rabbitmq.routing.transaction.completed_routingKey}")
	private String routingKey;
	
	private static final Logger logger = LoggerFactory.getLogger(TransactionEmailNotificationProducer.class);
	
	public void sendTransactionEmail(TransactionEmailNotificationEvent event) {
		try {
		   rabbitTemplate.convertAndSend(exchange, routingKey, event);
		   logger.info("Published transaction completed event for transaction Type: {}", event.getTransactionType().toUpperCase());
		}
		catch(Exception e) {
			logger.error("Error publishing transaction completed event", e);
		}
	}
}
/*
Component	Needs Queue?
Producer	❌ NO
Exchange	❌ NO
Binding	    ✅ YES
Consumer	✅ YES

Queue is declared and bound in configuration, not in producer:

@Bean
public Binding binding() {
    return BindingBuilder
        .bind(emailQueue())
        .to(emailExchange())
        .with(routingKey);
}

So RabbitMQ already knows:
(exchange + routingKey) → queue

*/
