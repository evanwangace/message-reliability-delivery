package com.evan.rabbitmq.common.converter;

import com.google.common.base.Preconditions;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.MessageProperties;
import org.springframework.amqp.support.converter.MessageConversionException;
import org.springframework.amqp.support.converter.MessageConverter;

/**
 * RabbitMessageConverter
 * 通过装饰者设计模式扩展delegate类
 *
 * @author evan
 * @date 2022-03-08
 */
public class RabbitMessageConverter implements MessageConverter {

    private final GenericMessageConverter delegate;

    public RabbitMessageConverter(GenericMessageConverter genericMessageConverter) {
        Preconditions.checkNotNull(genericMessageConverter);
        this.delegate = genericMessageConverter;
    }

    @Override
    public Message toMessage(Object object, MessageProperties messageProperties) throws MessageConversionException {
        com.evan.rabbitmq.api.Message message = (com.evan.rabbitmq.api.Message) object;
        messageProperties.setDelay(message.getDelayMills());
        return this.delegate.toMessage(object, messageProperties);
    }

    @Override
    public Object fromMessage(Message message) throws MessageConversionException {
        return this.delegate.fromMessage(message);
    }
}
