package com.evan.rabbitmq;

import java.util.List;

/**
 * 消息生产者接口
 *
 * @author evan
 * @date 2022-03-08
 */
public interface MessageProducer {
    /**
     * 附带SendCallback回调执行响应的业务逻辑处理的发送的消息发送
     *
     * @param message      消息
     * @param sendCallback 回调接口
     */
    void send(Message message, SendCallback sendCallback);

    /***
     * 简单的消息发送
     * @param message 消息
     */
    void send(Message message);

    /**
     * 批量的消息发送
     *
     * @param messages 消息集合
     */
    void send(List<Message> messages);
}
