package com.evan.rabbitmq.exception;

/**
 * 消息异常
 *
 * @author evan
 * @date 2022-03-08
 */
public class MessageException extends Exception {
    public MessageException() {
        super();
    }

    public MessageException(String message) {
        super(message);
    }

    public MessageException(String message, Throwable cause) {
        super(message, cause);
    }

    public MessageException(Throwable cause) {
        super(cause);
    }
}
