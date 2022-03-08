package com.evan.rabbitmq.producer.autoconfigure;

import com.evan.rabbitmq.producer.broker.ProducerClient;
import com.evan.rabbitmq.producer.broker.RabbitBroker;
import com.evan.rabbitmq.producer.broker.RabbitBrokerImpl;
import com.evan.rabbitmq.producer.broker.RabbitTemplateContainer;
import com.evan.rabbitmq.producer.mapper.BrokerMessageMapper;
import com.evan.rabbitmq.producer.service.MessageStoreService;
import com.evan.rabbitmq.producer.service.impl.MessageStoreServiceImpl;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * 自动装配
 *
 * @author evan
 * @date 2022-03-09
 */
@Configuration
public class RabbitProducerAutoConfiguration {

    @Bean
    public ProducerClient producerClient(RabbitBroker rabbitBroker) {
        return new ProducerClient(rabbitBroker);
    }

    @Bean
    public RabbitBroker rabbitBroker(RabbitTemplateContainer rabbitTemplateContainer, MessageStoreService messageStoreService) {
        return new RabbitBrokerImpl(rabbitTemplateContainer, messageStoreService);
    }

    @Bean
    public RabbitTemplateContainer rabbitTemplateContainer(ConnectionFactory connectionFactory, MessageStoreService messageStoreService) {
        return new RabbitTemplateContainer(connectionFactory, messageStoreService);
    }

    @Bean
    public MessageStoreService messageStoreService(BrokerMessageMapper brokerMessageMapper) {
        return new MessageStoreServiceImpl(brokerMessageMapper);
    }
}
