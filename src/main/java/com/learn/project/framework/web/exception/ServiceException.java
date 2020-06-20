package com.learn.project.framework.web.exception;

import com.alibaba.fastjson.JSON;
import com.learn.project.common.enums.ErrorState;
import com.learn.project.framework.web.domain.Result;
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

    private String message;

    public ServiceException() {
    }

    public ServiceException(ErrorState errorState) {
        this.message = JSON.toJSONString(Result.error(errorState));
    }

    public ServiceException(String message) {
        this.message = JSON.toJSONString(Result.error(ErrorState.GENERAL_EXCEPTION.getCode(), message));
    }

    public ServiceException(String message, Throwable cause) {
        super(message, cause);
    }

    public ServiceException(Throwable cause) {
        super(cause);
    }

}
