package com.evan.rabbitmq;

import com.google.common.base.Preconditions;
import com.google.common.base.Strings;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.HashMap;
import java.util.Map;

/**
 * 消息实体
 *
 * @author evan
 * @date 2022-03-08
 */
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Getter
public final class Message {
    /**
     * 消息的唯一ID
     */
    private final String messageId;

    /**
     * 消息的主题
     */
    private final String topic;

    /**
     * 消息的路由规则
     */
    private final String routingKey;

    /**
     * 消息的附加属性
     */
    private final Map<String, Object> attributes;

    /**
     * 延迟消息的参数配置
     */
    private final Integer delayMills;

    /**
     * 消息类型：默认为confirm消息类型
     */
    private final String messageType;

    public static Builder newBuilder(final String messageId, final String topic, final String routingKey) {
        return new Builder(messageId, topic, routingKey);
    }

    @RequiredArgsConstructor(access = AccessLevel.PRIVATE)
    public static class Builder {

        private final String messageId;

        private final String topic;

        private final String routingKey;

        private Map<String, Object> attributes = new HashMap<>();

        private Integer delayMills = 0;

        private String messageType = MessageType.CONFIRM;

        public Builder attributes(final Map<String, Object> attributes) {
            if (null != attributes) {
                this.attributes = attributes;
            }
            return this;
        }

        public Builder delayMills(final Integer delayMills) {
            if (null != delayMills) {
                this.delayMills = delayMills;
            }
            return this;
        }

        public Builder messageType(final String messageType) {
            if (null != messageType) {
                this.messageType = messageType;
            }
            return this;
        }

        public final Message build() {
            Preconditions.checkArgument(!Strings.isNullOrEmpty(messageId), "messageId can not be empty.");
            Preconditions.checkArgument(!Strings.isNullOrEmpty(topic), "topic can not be empty.");
            Preconditions.checkArgument(!Strings.isNullOrEmpty(routingKey), "routingKey can not be empty.");
            Preconditions.checkArgument(delayMills >= 0, "delayMills should larger than or equal to zero.");
            Preconditions.checkArgument(!Strings.isNullOrEmpty(messageType), "messageType can not be empty.");
            return new Message(messageId, topic, routingKey, attributes, delayMills, messageType);
        }
    }
}
