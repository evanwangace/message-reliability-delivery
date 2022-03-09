package com.evan.rabbitmq.producer.queue;

/**
 * 队列接口
 *
 * @author evan
 * @date 2022-03-09
 */
public interface Queue {
    /**
     * 提交方法
     *
     * @param runnable 线程
     */
    void submit(Runnable runnable);
}
