package com.evan.rabbitmq;

/**
 * 回调函数处理
 *
 * @author evan
 * @date 2022-03-08
 */
public interface SendCallback {
    /**
     * 成功回调
     */
    void onSuccess();

    /**
     * 失败回调
     */
    void onFailure();
}
