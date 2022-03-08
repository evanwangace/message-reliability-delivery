-- 表 broker_message.broker_message 结构
CREATE TABLE IF NOT EXISTS `broker_message`
(
    `message_id`  varchar(128) NOT NULL,
    `message`     varchar(4000),
    `try_count`   int(4)      DEFAULT 0,
    `status`      varchar(10) DEFAULT '',
    `next_retry`  timestamp    NOT NULL,
    `create_time` timestamp    NOT NULL,
    `update_time` timestamp    NOT NULL,
    PRIMARY KEY (`message_id`)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8;