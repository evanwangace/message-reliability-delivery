package com.evan.rabbitmq.producer.task;


import com.dangdang.ddframe.job.api.ShardingContext;
import com.dangdang.ddframe.job.api.dataflow.DataflowJob;
import com.evan.rabbitmq.producer.broker.RabbitBroker;
import com.evan.rabbitmq.producer.entity.BrokerMessage;
import com.evan.rabbitmq.producer.enums.BrokerMessageStatus;
import com.evan.rabbitmq.producer.service.MessageStoreService;
import com.evan.rabbitmq.task.annotation.ElasticJobConfig;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.util.List;

/**
 * 消息重试定时任务
 *
 * @author evan
 * @date 2022-03-01
 */
@ElasticJobConfig(
        name = "com.evan.rabbitmq.producer.task.RetryMessageDataflowJob",
        cron = "0/10 * * * * ?",
        description = "可靠性投递消息补偿任务",
        overwrite = true,
        // 生产环境需要根据表的数量，来处理分片数，让每个分片去处理一些任务
        shardingTotalCount = 1)
@Slf4j
@RequiredArgsConstructor
public class RetryMessageDataflowJob implements DataflowJob<BrokerMessage> {

    private final MessageStoreService messageStoreService;

    private final RabbitBroker rabbitBroker;

    private static final int MAX_RETRY_COUNT = 3;

    @Override
    public List<BrokerMessage> fetchData(ShardingContext shardingContext) {
        List<BrokerMessage> list = messageStoreService.fetchTimeOutMessage4Retry(BrokerMessageStatus.SENDING);
        log.info("--------@@@@@ 抓取数据集合, 数量：	{} 	@@@@@@-----------", list.size());
        return list;
    }

    @Override
    public void processData(ShardingContext shardingContext, List<BrokerMessage> dataList) {
        dataList.forEach(brokerMessage -> {
            String messageId = brokerMessage.getMessageId();
            if (brokerMessage.getTryCount() >= MAX_RETRY_COUNT) {
                this.messageStoreService.failure(messageId);
                log.warn(" -----消息设置为最终失败，消息ID: {} -------", messageId);
            } else {
                // 每次重发的时候要更新一下try count字段
                this.messageStoreService.updateTryCount(messageId);
                // 重发消息
                this.rabbitBroker.reliantSend(brokerMessage.getMessage());
            }
        });
    }
}