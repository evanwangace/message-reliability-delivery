package com.evan.rabbitmq.producer.broker;

import com.evan.rabbitmq.api.Message;
import com.evan.rabbitmq.api.MessageProducer;
import com.evan.rabbitmq.api.MessageType;
import com.evan.rabbitmq.api.SendCallback;
import com.evan.rabbitmq.api.exception.MessageRunTimeException;
import com.google.common.base.Preconditions;
import lombok.RequiredArgsConstructor;

import java.util.List;

/**
 * 发送消息的实际实现类
 *
 * @author evan
 * @date 2022-03-09
 */
@RequiredArgsConstructor
public class ProducerClient implements MessageProducer {

    private final RabbitBroker rabbitBroker;

    @Override
    public void send(Message message) throws MessageRunTimeException {
        Preconditions.checkNotNull(message.getTopic());
        String messageType = message.getMessageType();
        switch (messageType) {
            case MessageType.RAPID:
                rabbitBroker.rapidSend(message);
                break;
            case MessageType.CONFIRM:
                rabbitBroker.confirmSend(message);
                break;
            case MessageType.RELIANT:
                rabbitBroker.reliantSend(message);
                break;
            default:
                break;
        }
    }

    /**
     * 批量发送消息
     *
     * @param messages 消息集合
     */
    @Override
    public void send(List<Message> messages) throws MessageRunTimeException {
        messages.forEach(message -> {
            message.setMessageType(MessageType.RAPID);
            MessageHolder.add(message);
        });
        rabbitBroker.sendMessages();
    }

    @Override
    public void send(Message message, SendCallback sendCallback) throws MessageRunTimeException {

    }
}
