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
public class AccountRabbitMQConfig {
		
		@Value("${rabbitmq.queue.created_queue}")
		private String queue;
		
		@Value("${rabbitmq.exchange.account_exchange}")
		private String exchange;
		
		@Value("${rabbitmq.routing.created_routingKey}")
		private String routingKey;
		
		@Bean
		public Queue queue() {
			return new Queue(queue);
		}
		
		@Bean
		public TopicExchange exchange() {
			return new TopicExchange(exchange);
		}
		
		@Bean
		public Binding binding() {
			return BindingBuilder.bind(queue()).to(exchange()).with(routingKey);
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