package com.evan.rabbitmq.producer.broker;

import com.evan.rabbitmq.api.Message;
import com.evan.rabbitmq.api.MessageType;
import com.evan.rabbitmq.producer.consts.BrokerMessageConst;
import com.evan.rabbitmq.producer.entity.BrokerMessage;
import com.evan.rabbitmq.producer.enums.BrokerMessageStatus;
import com.evan.rabbitmq.producer.service.MessageStoreService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.time.DateUtils;
import org.springframework.amqp.rabbit.connection.CorrelationData;
import org.springframework.amqp.rabbit.core.RabbitTemplate;

import java.util.Date;
import java.util.List;

import static com.evan.rabbitmq.producer.consts.BrokerMessageConst.CORRELATION_DATA_ID_TEMPLATE;

/**
 * 真正的发送不同类型的消息实现类
 *
 * @author evan
 * @date 2022-03-09
 */
@Slf4j
@RequiredArgsConstructor
public class RabbitBrokerImpl implements RabbitBroker {

    private final RabbitTemplateContainer rabbitTemplateContainer;

    private final MessageStoreService messageStoreService;

    /**
     * reliantSend可靠性消息
     *
     * @param message 消息
     */
    @Override
    public void reliantSend(Message message) {
        message.setMessageType(MessageType.RELIANT);
        // 判断数据库中是否存在当前消息
        BrokerMessage bm = messageStoreService.selectByMessageId(message.getMessageId());
        if (bm == null) {
            // 1.把数据库的消息发送日志先记录好
            Date now = new Date();
            BrokerMessage brokerMessage = new BrokerMessage();
            brokerMessage.setMessageId(message.getMessageId());
            brokerMessage.setStatus(BrokerMessageStatus.SENDING.getCode());
            // tryCount在最开始发送的时候不需要进行设置，第一次发送不算重试
            brokerMessage.setNextRetry(DateUtils.addMinutes(now, BrokerMessageConst.TIMEOUT));
            brokerMessage.setCreateTime(now);
            brokerMessage.setUpdateTime(now);
            brokerMessage.setMessage(message);
            messageStoreService.insert(brokerMessage);
        }
        // 2.执行真正的发送消息逻辑
        sendKernel(message);
    }

    /**
     * rapidSend迅速发消息
     *
     * @param message 消息
     */
    @Override
    public void rapidSend(Message message) {
        message.setMessageType(MessageType.RAPID);
        sendKernel(message);
    }

    /**
     * sendKernel发送消息的核心方法，使用异步线程池进行发送消息
     *
     * @param message 消息
     */
    private void sendKernel(Message message) {
        AsyncBaseQueue.submit(() -> {
            CorrelationData correlationData = new CorrelationData(String.format(CORRELATION_DATA_ID_TEMPLATE,
                    message.getMessageId(), System.currentTimeMillis(), message.getMessageType()));
            String topic = message.getTopic();
            String routingKey = message.getRoutingKey();
            RabbitTemplate rabbitTemplate = rabbitTemplateContainer.getTemplate(message);
            rabbitTemplate.convertAndSend(topic, routingKey, message, correlationData);
            log.info("#RabbitBrokerImpl.sendKernel# send to rabbitmq, messageId: {}", message.getMessageId());
        });
    }

    @Override
    public void confirmSend(Message message) {
        message.setMessageType(MessageType.CONFIRM);
        sendKernel(message);
    }

    @Override
    public void sendMessages() {
        List<Message> messages = MessageHolder.clear();
        messages.forEach(message -> MessageHolderAsyncQueue.submit(() -> {
            CorrelationData correlationData = new CorrelationData(String.format(CORRELATION_DATA_ID_TEMPLATE,
                    message.getMessageId(), System.currentTimeMillis(), message.getMessageType()));
            String topic = message.getTopic();
            String routingKey = message.getRoutingKey();
            RabbitTemplate rabbitTemplate = rabbitTemplateContainer.getTemplate(message);
            rabbitTemplate.convertAndSend(topic, routingKey, message, correlationData);
            log.info("#RabbitBrokerImpl.sendMessages# send to rabbitmq, messageId: {}", message.getMessageId());
        }));
    }
}
