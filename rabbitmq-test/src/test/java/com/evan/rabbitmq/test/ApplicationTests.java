package com.evan.rabbitmq.test;

import com.evan.rabbitmq.api.Message;
import com.evan.rabbitmq.api.MessageType;
import com.evan.rabbitmq.producer.broker.ProducerClient;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@RunWith(SpringRunner.class)
@SpringBootTest
public class ApplicationTests {

    @Autowired
    private ProducerClient producerClient;

    @Test
    public void testNormalProducerClient() throws Exception {
        for (int i = 0; i < 1; i++) {
            String uniqueId = UUID.randomUUID().toString();
            Map<String, Object> attributes = new HashMap<>();
            attributes.put("name", "张三");
            attributes.put("age", "18");
            Message message = Message.newBuilder(uniqueId,
                    "exchange-2",
                    "springboot.abc")
                    .attributes(attributes)
                    .build();
            message.setMessageType(MessageType.RELIANT);
            producerClient.send(message);
        }
        Thread.sleep(100000);
    }

    @Test
    public void testDelayedProducerClient() throws Exception {
        for (int i = 0; i < 1; i++) {
            String uniqueId = UUID.randomUUID().toString();
            Map<String, Object> attributes = new HashMap<>();
            attributes.put("name", "李四");
            attributes.put("age", "20");
            Message message = Message.newBuilder(
                    uniqueId,
                    "delay-exchange",
                    "delay.abc")
                    .attributes(attributes)
                    .delayMills(15000)
                    .build();
            message.setMessageType(MessageType.RELIANT);
            producerClient.send(message);
        }
        Thread.sleep(100000);
    }
}
