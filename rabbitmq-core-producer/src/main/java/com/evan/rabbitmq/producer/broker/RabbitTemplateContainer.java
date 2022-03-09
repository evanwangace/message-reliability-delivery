package com.evan.rabbitmq.producer.broker;

import com.evan.rabbitmq.api.Message;
import com.evan.rabbitmq.api.MessageType;
import com.evan.rabbitmq.common.converter.GenericMessageConverter;
import com.evan.rabbitmq.common.converter.RabbitMessageConverter;
import com.evan.rabbitmq.common.serializer.Serializer;
import com.evan.rabbitmq.common.serializer.SerializerFactory;
import com.evan.rabbitmq.common.serializer.impl.JacksonSerializerFactory;
import com.evan.rabbitmq.producer.service.MessageStoreService;
import com.google.common.base.Preconditions;
import com.google.common.base.Splitter;
import com.google.common.collect.Maps;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.connection.CorrelationData;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.retry.support.RetryTemplate;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;

/**
 * RabbitTemplateContainer池化封装
 * 每一个topic 对应一个RabbitTemplate
 * 1.提高发送的效率
 * 2.可以根据不同的需求制定化不同的RabbitTemplate，比如每一个topic 都有自己的routingKey规则
 * 补充：如果想发带事务的message，再封装一个message类型就可以了，然后根据type判断，进行相应处理。
 *
 * @author evan
 * @date 2022-03-09
 */
@Slf4j
@RequiredArgsConstructor
public class RabbitTemplateContainer implements RabbitTemplate.ConfirmCallback {
    /**
     * key:Topic
     */
    private final Map<String, RabbitTemplate> rabbitMap = Maps.newConcurrentMap();

    private final Splitter splitter = Splitter.on("#");

    private final SerializerFactory serializerFactory = JacksonSerializerFactory.INSTANCE;

    private final ConnectionFactory connectionFactory;

    private final MessageStoreService messageStoreService;

    public RabbitTemplate getTemplate(Message message) {
        Preconditions.checkNotNull(message);
        String topic = message.getTopic();
        RabbitTemplate rabbitTemplate = rabbitMap.get(topic);
        if (rabbitTemplate != null) {
            return rabbitTemplate;
        }
        log.info("#RabbitTemplateContainer.getTemplate# topic: {} is not exists, create one", topic);

        RabbitTemplate newTemplate = new RabbitTemplate(connectionFactory);
        newTemplate.setExchange(topic);
        newTemplate.setRoutingKey(message.getRoutingKey());
        newTemplate.setRetryTemplate(new RetryTemplate());

        // 添加序列化反序列化和converter对象
        Serializer serializer = serializerFactory.create();
        GenericMessageConverter gmc = new GenericMessageConverter(serializer);
        RabbitMessageConverter rmc = new RabbitMessageConverter(gmc);
        newTemplate.setMessageConverter(rmc);

        String messageType = message.getMessageType();
        // 除了迅速应答，都需要对消息进行确认应答
        if (!MessageType.RAPID.equals(messageType)) {
            newTemplate.setConfirmCallback(this);
        }

        rabbitMap.putIfAbsent(topic, newTemplate);

        return rabbitMap.get(topic);
    }

    /**
     * 无论是 confirm 消息 还是 reliant 消息 ，发送消息以后 broker都会去回调confirm
     */
    @Override
    public void confirm(CorrelationData correlationData, boolean ack, String cause) {
        // 具体的消息应答
        List<String> strings = splitter.splitToList(correlationData.getId());
        String messageId = strings.get(0);
        long sendTime = Long.parseLong(strings.get(1));
        String messageType = strings.get(2);
        if (ack) {
            // 当Broker返回ACK成功时，就是更新一下日志表里对应的消息发送状态为 SEND_OK
            // 如果当前消息类型为reliant 我们就去数据库查找并进行更新
            if (MessageType.RELIANT.endsWith(messageType)) {
                this.messageStoreService.success(messageId);
            }
            log.info("send message is OK, confirm messageId: {}, sendTime: {}", messageId, sendTime);
        } else {
            //TODO 拓展，当队列满了，再次重试也无意义，这时候我们要丰富失败原因（BrokerMessageStatus），根据失败的具体原因进行相应的处理。
            log.error("send message is Fail, confirm messageId: {}, sendTime: {}", messageId, sendTime);
        }
    }
}
