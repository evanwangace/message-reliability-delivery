package com.evan.rabbitmq.task.annotation;

import org.springframework.core.annotation.AliasFor;
import org.springframework.stereotype.Component;

import java.lang.annotation.*;

/**
 * elastic-job配置自定义注解
 * 详细配置参考：https://shardingsphere.apache.org/elasticjob/legacy/lite-2.x/02-guide/config-manual/
 *
 * @author evan
 * @date 2022-03-08
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Component
public @interface ElasticJobConfig {
    /**
     * 任务名称
     */
    @AliasFor(annotation = Component.class, attribute = "value")
    String name();

    String cron() default "";

    int shardingTotalCount() default 1;

    String shardingItemParameters() default "";

    String jobParameter() default "";

    boolean failover() default false;

    boolean misfire() default true;

    String description() default "";

    boolean overwrite() default false;

    boolean streamingProcess() default false;

    String scriptCommandLine() default "";

    boolean monitorExecution() default false;

    /**
     * 作业监控端口
     * 建议配置作业监控端口, 方便开发者dump作业信息。
     * 使用方法: echo “dump” | nc 127.0.0.1 9888
     * （必填项）
     */
    int monitorPort() default -1;

    /**
     * 最大允许的本机与注册中心的时间误差秒数
     * 如果时间误差超过配置秒数则作业启动时将抛异常
     * 配置为-1表示不校验时间误差
     * （必填项）
     */
    int maxTimeDiffSeconds() default -1;

    /**
     * 作业分片策略实现类全路径
     * 默认使用平均分配策略
     * （必填项）
     */
    String jobShardingStrategyClass() default "";

    /**
     * 修复作业服务器不一致状态服务调度间隔时间，配置为小于1的任意值表示不执行修复
     * 单位：分钟
     * （必填项）
     */
    int reconcileIntervalMinutes() default 10;

    /**
     * 作业事件追踪的数据源Bean引用
     * （必填项）
     */
    String eventTraceRdbDataSource() default "";

    /**
     * 前置后置任务监听实现类，需实现ElasticJobListener接口
     * （必填项）
     */
    String listener() default "";

    /**
     * 作业是否禁止启动
     * 可用于部署作业时，先禁止启动，部署结束后统一启动
     * （必填项）
     */
    boolean disabled() default false;

    String distributedListener() default "";

    /**
     * 最后一个作业执行前的执行方法的超时时间
     * 单位：毫秒
     * （必填项）
     */
    long startedTimeoutMilliseconds() default Long.MAX_VALUE;

    /**
     * 最后一个作业执行前的执行方法的超时时间
     * 单位：毫秒
     * （必填项）
     */
    long completedTimeoutMilliseconds() default Long.MAX_VALUE;

    String jobExceptionHandler() default "com.dangdang.ddframe.job.executor.handler.impl.DefaultJobExceptionHandler";

    String executorServiceHandler() default "com.dangdang.ddframe.job.executor.handler.impl.DefaultExecutorServiceHandler";
}
