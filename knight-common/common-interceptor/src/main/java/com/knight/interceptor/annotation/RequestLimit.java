package com.knight.interceptor.annotation;

import com.knight.entity.enums.ErrorState;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 请求限制，接口防刷
 *
 * @author lixiao
 * @version 1.0
 * @date 2020/6/22 9:25
 */
@Documented
@Inherited
@Target({ ElementType.METHOD, ElementType.TYPE })
@Retention(RetentionPolicy.RUNTIME)
public @interface RequestLimit {

	/**
	 * 默认为5秒内可访问1次
	 */
	int second() default 5;

	int maxCount() default 1;

	/**
	 * limit key
	 * @return {@link String}
	 */
	String key() default "";

	/**
	 * 自定义limitKey生成器 {@link com.knight.interceptor.LimitKeyGenerator} 与key属性互斥
	 */
	String keyGenerator() default "";

	/**
	 * 超出限制后，返回的错误提示
	 * @return msg
	 */
	ErrorState msg() default ErrorState.REQUEST_LIMIT;

}
