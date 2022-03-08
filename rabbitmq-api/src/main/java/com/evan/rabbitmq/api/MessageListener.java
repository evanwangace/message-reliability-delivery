package com.evan.rabbitmq.api;

/**
 * 消费者监听消息
 *
 * @author evan
 * @date 2022-03-08
 */
public interface MessageListener {
    /**
     * 收到消息处理方法
     *
     * @param message 消息
     */
    void onMessage(Message message);
}
