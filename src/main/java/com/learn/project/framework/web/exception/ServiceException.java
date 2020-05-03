package com.learn.project.framework.web.exception;

import com.learn.project.common.enums.ErrorState;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * @author lixiao
 * @version 1.0
 * @date 2020/4/30 16:31
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class ServiceException extends RuntimeException {

    private Integer code;
    private String message;

    public ServiceException() {
    }

    public ServiceException(String message) {
        this.code = ErrorState.GENERAL_EXCEPTION.getCode();
        this.message = message;
    }

    public ServiceException(Integer code, String message) {
        this.code = code;
        this.message = message;
    }

    public ServiceException(String message, Throwable cause) {
        super(message, cause);
    }

    public ServiceException(Throwable cause) {
        super(cause);
    }

}
