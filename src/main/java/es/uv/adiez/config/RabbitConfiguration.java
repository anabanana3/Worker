package es.uv.adiez.config;

import org.springframework.amqp.rabbit.connection.CachingConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitAdmin;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.rabbit.listener.SimpleMessageListenerContainer;
import org.springframework.amqp.rabbit.listener.adapter.MessageListenerAdapter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import es.uv.adiez.consumer.MessageConsumer;


@Configuration
public class RabbitConfiguration {
	@Value("${jsa.rabbitmq.exchange}")
	private String topicExchangeName;

	@Value("${jsa.rabbitmq.queue}")
	private String queueName;

	@Value("${jsa.rabbitmq.routingKey}")
	private String routingKey;
	@Value("${jsa.rabbitmq.host}")
	private String host;

    @Bean
    public CachingConnectionFactory connectionFactory() {
        return new CachingConnectionFactory(this.host);
    }

    @Bean
    public RabbitAdmin amqpAdmin() {
        return new RabbitAdmin(connectionFactory());
    }

    @Bean
    public RabbitTemplate rabbitTemplate() {
        return new RabbitTemplate(connectionFactory());
    }

    @Bean
    SimpleMessageListenerContainer messageListenerContainer(CachingConnectionFactory connectionFactory, MessageListenerAdapter listenerAdapter) {
        SimpleMessageListenerContainer simpleMessageListenerContainer = new SimpleMessageListenerContainer();
        simpleMessageListenerContainer.setConnectionFactory(connectionFactory);
        simpleMessageListenerContainer.setQueueNames(this.queueName);
        simpleMessageListenerContainer.setMessageListener(listenerAdapter);
        return simpleMessageListenerContainer;
    }
  
    
    @Bean
    MessageListenerAdapter listenerAdapter(MessageConsumer messageConsumerService) {
        return new MessageListenerAdapter(messageConsumerService, "consumeMessage");
    }
}
