package com.evan.rabbitmq.task.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 任务类型枚举类
 *
 * @author evan
 * @date 2022-03-08
 */
@Getter
@AllArgsConstructor
public enum ElasticJobTypeEnum {
    /**
     * 简单任务
     */
    SIMPLE("SimpleJob", "简单类型job"),

    /**
     * 流任务
     */
    DATAFLOW("DataflowJob", "流式类型job"),

    /**
     * 脚本任务
     */
    SCRIPT("ScriptJob", "脚本类型job");

    private final String type;

    private final String desc;
}
