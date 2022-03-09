package com.evan.rabbitmq.producer.queue;

import com.google.common.base.Preconditions;

import java.util.HashMap;
import java.util.Map;

import static com.evan.rabbitmq.producer.consts.BrokerMessageConst.ASYNC_BASE_QUEUE;

/**
 * 队列工厂
 *
 * @author evan
 * @date 2022-03-09
 */
public class QueueFactory {

    private final static Map<String, Queue> QUEUE_MAP = new HashMap<>();

    static {
        QUEUE_MAP.put(ASYNC_BASE_QUEUE, new AsyncBaseQueue());
    }

    public static Queue getQueue(String queueName) {
        Queue queue = QUEUE_MAP.get(queueName);
        Preconditions.checkNotNull(queue, "queueName:" + queueName + " is null, get failed");
        return queue;
    }
}
