package com.example.demo.consumer;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.example.demo.dtos.AccountEmailNotificationEvent;
import com.example.demo.dtos.TransactionEmailNotificationEvent;
import com.example.demo.service.EmailNotificationService;

@Component
public class EmailNotificationListener {
	
	@Autowired
	private EmailNotificationService emailNotificationService;
	
	private static final Logger logger = LoggerFactory.getLogger(EmailNotificationListener.class);
	
	@RabbitListener(queues = "${rabbitmq.queue.created_queue}")
	public void receiveAccountEmail(AccountEmailNotificationEvent event) {
		try {
		emailNotificationService.sendAccountCreatedMail(event);
		logger.info("Successfully processed account created event for: {}", event.getEmail());
		}
		catch(Exception e) {
			logger.error("Error processing account created event", e);
			throw e;
		}
	}
	
	@RabbitListener(queues = "${rabbitmq.queue.transaction.completed_queue}")
	public void receiveTransactionEmail(TransactionEmailNotificationEvent event) {
		try {
			emailNotificationService.sendTransactionCompletedMal(event);
			logger.info("Successfully processed transaction completed event for: {}", event.getEmail());
		}
		catch(Exception e) {
			logger.error("Error processing transaction completed event", e);
			throw e;
		}
	}
	
	/* For checking the rabbitMQ queue data which is in bytes and 
	  if i call this method it will give queue data in json format
	@RabbitListener(queues = "${rabbitmq.queue.created_queue}")
	public void debug(byte[] message) {
		System.out.println(new String(message));
	}*/
}
