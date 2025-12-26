package com.example.demo.producer;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.example.demo.dtos.AccountEmailNotificationEvent;
	
@Service
public class AccountEmailNotificationProducer {
	
		@Autowired
		private RabbitTemplate rabbitTemplate;
		
		@Value("${rabbitmq.exchange.account_exchange}")
		private String exchange;
		
		@Value("${rabbitmq.routing.created_routingKey}")
		private String routingKey;
		
		private static final Logger logger = LoggerFactory.getLogger(AccountEmailNotificationProducer.class);
		
		public void sendAccountEmail(AccountEmailNotificationEvent event) {
			try {
			   rabbitTemplate.convertAndSend(exchange, routingKey, event);
			   logger.info("Published account creation event for accountEmail: {}", event.getEmail());
			}
			catch(Exception e) {
				logger.error("Error publishing account created event", e);
			}
		}
	}

