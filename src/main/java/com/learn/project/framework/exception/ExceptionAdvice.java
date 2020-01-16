package com.learn.project.framework.exception;


import com.aliyuncs.exceptions.ClientException;
import com.learn.project.common.enums.ErrorState;
import com.learn.project.framework.Result;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.authc.IncorrectCredentialsException;
import org.apache.shiro.authz.AuthorizationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import javax.validation.ConstraintViolationException;


/**
 * @author lixiao
 * @date 2019/8/7 11:09
 */
@RestControllerAdvice
@Slf4j
public class ExceptionAdvice {

    /**
     * 参数校验异常
     * @param e e
     * @return 错误信息
     */
    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<String> handleConstraintViolationException(ConstraintViolationException e) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
    }

    /**
     * 阿里短信发送异常
     * @return result
     */
    @ExceptionHandler(ClientException.class)
    public Result handleClientException(){
        return Result.error(ErrorState.SEND_SMS_ERROR);
    }

    
    /**
     * shiro权限异常处理
     * @return result
     */
    @ExceptionHandler(AuthorizationException.class)
    public ResponseEntity<String> handleShiroException() {
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(ErrorState.NOT_AUTH.getMsg());
    }


    /**
     * token无效异常
     */
    @ExceptionHandler(IncorrectCredentialsException.class)
    public Result handleTokenException(){
        return Result.error(ErrorState.TOKEN_INVALID);
    }


    /**
     * 参数校验异常处理
     * @return result
     */
    @ExceptionHandler(MissingServletRequestParameterException.class)
    public Result handleMissingParameterException(){
        return Result.error(ErrorState.MISSING_PARAMETER);
    }


    /**
     * redis异常
     * @return REDIS_CONNECTION_FAILURE
     */
    @ExceptionHandler(RedisException.class)
    public Result handleRedisException(){
        return Result.error(ErrorState.REDIS_CONNECTION_FAILURE);
    }

}
