package com.evan.rabbitmq.producer.entity;

import com.evan.rabbitmq.api.Message;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.util.Date;

/**
 * 消息记录表实体
 *
 * @author evan
 * @date 2022-03-09
 */
@Getter
@Setter
public class BrokerMessage implements Serializable {

    private String messageId;

    private Message message;

    private Integer tryCount = 0;

    private String status;

    private Date nextRetry;

    private Date createTime;

    private Date updateTime;
}
