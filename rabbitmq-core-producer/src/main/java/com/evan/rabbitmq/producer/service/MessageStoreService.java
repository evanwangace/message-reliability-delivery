package com.evan.rabbitmq.producer.service;

import com.evan.rabbitmq.producer.entity.BrokerMessage;
import com.evan.rabbitmq.producer.enums.BrokerMessageStatus;

import java.util.List;

/**
 * 消息存储Service
 *
 * @author evan
 * @date 2022-03-09
 */
public interface MessageStoreService {
    /**
     * 插入消息
     *
     * @param brokerMessage 消息记录表实体
     * @return 受影响行数
     */
    int insert(BrokerMessage brokerMessage);

    /**
     * 根据消息id查询消息
     *
     * @param messageId 消息id
     * @return 消息记录实体
     */
    BrokerMessage selectByMessageId(String messageId);

    /**
     * 根据消息id将消息记录状态修改为成功
     *
     * @param messageId 消息id
     */
    void success(String messageId);

    /**
     * 根据消息id将消息记录状态修改为失败
     *
     * @param messageId 消息id
     */
    void failure(String messageId);

    /**
     * 获取需要重试的消息集合
     *
     * @param brokerMessageStatus 消息记录状态
     * @return 消息记录集合
     */
    List<BrokerMessage> fetchTimeOutMessage4Retry(BrokerMessageStatus brokerMessageStatus);

    /**
     * 根据消息记录id更新消息记录重试数量
     *
     * @param brokerMessageId 消息记录id
     * @return 受影响的行数
     */
    int updateTryCount(String brokerMessageId);
}
