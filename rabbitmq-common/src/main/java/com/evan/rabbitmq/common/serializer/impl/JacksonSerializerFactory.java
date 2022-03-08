package com.evan.rabbitmq.common.serializer.impl;


import com.evan.rabbitmq.api.Message;
import com.evan.rabbitmq.common.serializer.Serializer;
import com.evan.rabbitmq.common.serializer.SerializerFactory;

/**
 * 序列化工厂实现类
 *
 * @author evan
 * @date 2022-03-08
 */
public class JacksonSerializerFactory implements SerializerFactory {

    public static final SerializerFactory INSTANCE = new JacksonSerializerFactory();

    @Override
    public Serializer create() {
        return JacksonSerializer.createParametricType(Message.class);
    }
}
