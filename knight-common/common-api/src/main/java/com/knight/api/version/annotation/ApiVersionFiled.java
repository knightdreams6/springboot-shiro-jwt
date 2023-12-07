package com.knight.api.version.annotation;

import com.knight.api.version.component.VersionCompareStrategy;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * api版本
 *
 * @author knight
 * @since 2023/12/06
 */
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
public @interface ApiVersionFiled {

	/**
	 * 当前字段版本
	 * @return int
	 */
	int value() default 1;

	/**
	 * 版本比较策略
	 * @return {@link VersionCompareStrategy}
	 */
	VersionCompareStrategy strategy() default VersionCompareStrategy.GE;

}
