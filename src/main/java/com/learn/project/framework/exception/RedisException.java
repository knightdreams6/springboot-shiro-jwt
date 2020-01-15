package com.learn.project.framework.exception;

/**
 * @author lixiao
 * @date 2019/10/26 15:12
 */
public class RedisException extends RuntimeException {


    public RedisException(){
        super();
    }

    /**
     * Create a {@code RedisException} with the specified detail message.
     *
     * @param msg the detail message.
     */
    public RedisException(String msg) {
        super(msg);
    }

    /**
     * Create a {@code RedisException} with the specified detail message and nested exception.
     *
     * @param msg the detail message.
     * @param cause the nested exception.
     */
    public RedisException(String msg, Throwable cause) {
        super(msg, cause);
    }

    /**
     * Create a {@code RedisException} with the specified nested exception.
     *
     * @param cause the nested exception.
     */
    public RedisException(Throwable cause) {
        super(cause);
    }
}
