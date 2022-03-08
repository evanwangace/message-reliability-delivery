package com.evan.rabbitmq.api.exception;

/**
 * 消息运行时异常
 *
 * @author evan
 * @date 2022-03-08
 */
public class MessageRunTimeException extends RuntimeException {
    public MessageRunTimeException() {
        super();
    }

    public MessageRunTimeException(String message) {
        super(message);
    }

    public MessageRunTimeException(String message, Throwable cause) {
        super(message, cause);
    }

    public MessageRunTimeException(Throwable cause) {
        super(cause);
    }
}
