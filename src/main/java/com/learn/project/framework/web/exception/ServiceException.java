package com.learn.project.framework.web.exception;

/**
 * @author lixiao
 * @version 1.0
 * @date 2020/4/30 16:31
 */
public class ServiceException extends RuntimeException {

    public ServiceException() {
    }

    public ServiceException(String message) {
        super(message);
    }

    public ServiceException(String message, Throwable cause) {
        super(message, cause);
    }

    public ServiceException(Throwable cause) {
        super(cause);
    }

}
