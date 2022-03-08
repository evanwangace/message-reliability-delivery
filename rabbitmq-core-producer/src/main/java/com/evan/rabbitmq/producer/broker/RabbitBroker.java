package com.evan.rabbitmq.producer.broker;


import com.evan.rabbitmq.api.Message;

/**
 * 具体发送不同种类型消息的接口
 *
 * @author evan
 * @date 2022-03-09
 */
public interface RabbitBroker {
    /**
     * 发送迅速消息
     *
     * @param message 消息
     */
    void rapidSend(Message message);

    /**
     * 发送确认消息
     *
     * @param message 消息
     */
    void confirmSend(Message message);

    /**
     * 发送可靠消息
     *
     * @param message 可靠消息
     */
    void reliantSend(Message message);

    /**
     * 批量发送消息
     */
    void sendMessages();
}
