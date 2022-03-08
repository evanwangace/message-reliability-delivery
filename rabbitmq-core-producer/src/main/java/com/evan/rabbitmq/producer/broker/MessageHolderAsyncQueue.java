package com.evan.rabbitmq.producer.broker;

import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * 批量发送消息载体异步队列
 *
 * @author evan
 * @date 2022-03-02
 */
@Slf4j
public class MessageHolderAsyncQueue {

    private static final int THREAD_SIZE = Runtime.getRuntime().availableProcessors();

    private static final int QUEUE_SIZE = 10000;

    private static final ExecutorService SENDER_ASYNC =
            new ThreadPoolExecutor(THREAD_SIZE,
                    THREAD_SIZE,
                    60L,
                    TimeUnit.SECONDS,
                    new ArrayBlockingQueue<Runnable>(QUEUE_SIZE),
                    r -> {
                        Thread t = new Thread(r);
                        t.setName("rabbitmq_client_async_sender");
                        return t;
                    },
                    (r, executor) -> log.error("async sender is error rejected, runnable: {}, executor: {}", r, executor));

    public static void submit(Runnable runnable) {
        SENDER_ASYNC.submit(runnable);
    }
}
