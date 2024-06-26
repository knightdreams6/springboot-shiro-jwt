package com.knight.api.limit.component;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.web.method.HandlerMethod;

/**
 * 限制key生成器
 *
 * @author knight
 * @since 2022/04/11
 */
@FunctionalInterface
public interface LimitKeyGenerator {

	/**
	 * 生成唯一key
	 * @param request 请求
	 * @param handlerMethod 处理程序方法
	 * @return {@link String}
	 */
	String generate(HttpServletRequest request, HandlerMethod handlerMethod);

}
