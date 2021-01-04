package com.learn.project.framework.web.exception;


import com.alibaba.fastjson.JSONObject;
import com.aliyuncs.exceptions.ClientException;
import com.learn.project.common.enums.ErrorState;
import com.learn.project.framework.web.domain.Result;
import org.apache.shiro.authc.IncorrectCredentialsException;
import org.apache.shiro.authz.AuthorizationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
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
public class GlobalExceptionAdvice {

    /**
     * 通用业务异常
     *
     * @param e e
     * @return ResponseEntity
     */
    @ExceptionHandler(ServiceException.class)
    public ResponseEntity<String> handleServiceException(ServiceException e) {
        return ResponseEntity.status(HttpStatus.OK).contentType(MediaType.APPLICATION_JSON)
                .body(e.getMessage());
    }


    /**
     * 参数校验异常
     *
     * @param e e
     * @return ResponseEntity
     */
    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<String> handleConstraintViolationException(ConstraintViolationException e) {
        return ResponseEntity.status(HttpStatus.OK).contentType(MediaType.APPLICATION_JSON)
                .body(e.getMessage());
    }


    /**
     * 阿里短信发送异常
     *
     * @return ResponseEntity
     */
    @ExceptionHandler(ClientException.class)
    public ResponseEntity<String> handleClientException() {
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).contentType(MediaType.APPLICATION_JSON)
                .body(JSONObject.toJSONString(Result.error(ErrorState.SEND_SMS_ERROR)));
    }


    /**
     * shiro权限异常处理
     *
     * @return ResponseEntity
     */
    @ExceptionHandler(AuthorizationException.class)
    public ResponseEntity<String> handleShiroException() {
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).contentType(MediaType.APPLICATION_JSON)
                .body(JSONObject.toJSONString(Result.error(ErrorState.NOT_AUTH)));
    }


    /**
     * token无效异常
     */
    @ExceptionHandler(IncorrectCredentialsException.class)
    public ResponseEntity<String> handleTokenException() {
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).contentType(MediaType.APPLICATION_JSON)
                .body(JSONObject.toJSONString(Result.error(ErrorState.TOKEN_INVALID)));
    }


    /**
     * 参数校验(缺少)异常处理
     *
     * @return ResponseEntity
     */
    @ExceptionHandler(MissingServletRequestParameterException.class)
    public ResponseEntity<String> handleMissingParameterException() {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).contentType(MediaType.APPLICATION_JSON)
                .body(JSONObject.toJSONString(Result.error(ErrorState.MISSING_PARAMETER)));
    }


}
