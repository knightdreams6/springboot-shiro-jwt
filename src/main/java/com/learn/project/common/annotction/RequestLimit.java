package com.learn.project.common.annotction;

import com.learn.project.common.enums.ErrorState;

import java.lang.annotation.*;

/**
 * 请求限制，接口防刷
 * @author lixiao
 * @version 1.0
 * @date 2020/6/22 9:25
 */
@Documented
@Inherited
@Target({ElementType.METHOD, ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
public @interface RequestLimit {

    /**
     * 默认为5秒内可访问1次
     */
    int second() default 5;

    int maxCount() default 1;

    /**
     * 超出限制后，返回的错误提示
     * @return msg
     */
    ErrorState msg() default ErrorState.REQUEST_LIMIT;
}
