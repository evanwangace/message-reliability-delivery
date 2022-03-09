package com.evan.rabbitmq.producer.consts;

/**
 * 常量信息
 *
 * @author evan
 * @date 2022-03-09
 */
public class BrokerMessageConst {
    /**
     * 重试的时间间隔
     */
    public static final Integer TIMEOUT = 1;

    /**
     * correlationData id组合模板字符串
     */
    public static final String CORRELATION_DATA_ID_TEMPLATE = "%s#%s#%s";
}