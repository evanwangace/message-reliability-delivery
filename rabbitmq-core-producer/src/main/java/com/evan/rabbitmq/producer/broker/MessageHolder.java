package com.evan.rabbitmq.producer.broker;

import com.evan.rabbitmq.api.Message;
import com.google.common.collect.Lists;

import java.util.List;

/**
 * 批量发送消息载体
 *
 * @author evan
 * @date 2022-03-09
 */
public class MessageHolder {

    private final List<Message> messages = Lists.newArrayList();

    public static final ThreadLocal<MessageHolder> HOLDER = ThreadLocal.withInitial(MessageHolder::new);

    public static void add(Message message) {
        HOLDER.get().messages.add(message);
    }

    public static List<Message> clear() {
        List<Message> tmp = Lists.newArrayList(HOLDER.get().messages);
        HOLDER.remove();
        return tmp;
    }
}
