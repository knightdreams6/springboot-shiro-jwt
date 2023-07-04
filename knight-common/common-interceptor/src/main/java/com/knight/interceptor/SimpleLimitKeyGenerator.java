package com.knight.interceptor;

import org.springframework.util.DigestUtils;
import org.springframework.web.method.HandlerMethod;

import javax.servlet.http.HttpServletRequest;
import java.util.Arrays;
import java.util.stream.Collectors;

/**
 * 简单限制key生成器
 *
 * @author knight
 * @since 2022/04/11
 */
public class SimpleLimitKeyGenerator implements LimitKeyGenerator {

	/**
	 * 限制key前缀
	 */
	private static final String LIMIT_KEY_PREFIX = "LIMIT:";

	@Override
	public String generate(HttpServletRequest request, HandlerMethod handlerMethod) {
		// 接口地址
		String servletPath = request.getServletPath();
		// 参数列表
		String params = request.getParameterMap()
			.values()
			.stream()
			.flatMap(Arrays::stream)
			.collect(Collectors.joining());
		// 接口地址 + 参数列表
		String key = servletPath + params;
		// md5
		return LIMIT_KEY_PREFIX + DigestUtils.md5DigestAsHex(key.getBytes());
	}

}
