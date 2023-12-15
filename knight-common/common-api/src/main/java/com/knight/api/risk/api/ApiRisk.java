package com.knight.api.risk.api;

import java.lang.annotation.ElementType;
import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.util.concurrent.TimeUnit;

/**
 * api风险认证
 *
 * @author knight
 * @since 2023/12/14
 */
@Inherited
@Target({ ElementType.METHOD })
@Retention(RetentionPolicy.RUNTIME)
public @interface ApiRisk {

	/**
	 * @return {@link ApiRiskVerificationType}
	 */
	ApiRiskVerificationType verifyType() default ApiRiskVerificationType.PASSWORD;

	/**
	 * 过期时间
	 * @return long
	 */
	long expired() default 5L;

	/**
	 * 过期时间单位
	 * @return {@link TimeUnit}
	 */
	TimeUnit expiredUnit() default TimeUnit.MINUTES;

}
