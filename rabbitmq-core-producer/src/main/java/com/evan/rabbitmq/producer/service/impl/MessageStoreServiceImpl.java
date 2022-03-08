package com.evan.rabbitmq.producer.service.impl;

import com.evan.rabbitmq.producer.entity.BrokerMessage;
import com.evan.rabbitmq.producer.enums.BrokerMessageStatus;
import com.evan.rabbitmq.producer.mapper.BrokerMessageMapper;
import com.evan.rabbitmq.producer.service.MessageStoreService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

/**
 * 消息存储Service实现类
 *
 * @author evan
 * @date 2022-03-09
 */
@RequiredArgsConstructor
public class MessageStoreServiceImpl implements MessageStoreService {

    private final BrokerMessageMapper brokerMessageMapper;

    @Override
    public int insert(BrokerMessage brokerMessage) {
        return this.brokerMessageMapper.insert(brokerMessage);
    }

    @Override
    public BrokerMessage selectByMessageId(String messageId) {
        return this.brokerMessageMapper.selectByPrimaryKey(messageId);
    }

    @Override
    public void success(String messageId) {
        this.brokerMessageMapper.changeBrokerMessageStatus(messageId,
                BrokerMessageStatus.SEND_OK.getCode(),
                new Date());
    }

    @Override
    public void failure(String messageId) {
        this.brokerMessageMapper.changeBrokerMessageStatus(messageId,
                BrokerMessageStatus.SEND_FAIL.getCode(),
                new Date());
    }

    @Override
    public List<BrokerMessage> fetchTimeOutMessage4Retry(BrokerMessageStatus brokerMessageStatus) {
        return this.brokerMessageMapper.queryBrokerMessageStatus4Timeout(brokerMessageStatus.getCode());
    }

    @Override
    public int updateTryCount(String brokerMessageId) {
        return this.brokerMessageMapper.update4TryCount(brokerMessageId, new Date());
    }
}
