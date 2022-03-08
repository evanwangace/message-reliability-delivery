package com.evan.rabbitmq.producer.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 消息的发送状态
 *
 * @author evan
 * @date 2022-03-09
 */
@AllArgsConstructor
@Getter
public enum BrokerMessageStatus {
    /**
     * 发送中
     */
    SENDING("0"),

    /**
     * 发送成功
     */
    SEND_OK("1"),

    /**
     * 发送失败
     */
    SEND_FAIL("2"),

    /**
     * 发送闪断
     */
    SEND_FAIL_A_MOMENT("3");

    private final String code;
}

