package com.evan.rabbitmq.common.serializer;

/**
 * 序列化工厂接口
 *
 * @author evan
 * @date 2022-03-08
 */
public interface SerializerFactory {
    /**
     * 创建serializer
     *
     * @return Serializer
     */
    com.evan.rabbitmq.common.serializer.Serializer create();
}