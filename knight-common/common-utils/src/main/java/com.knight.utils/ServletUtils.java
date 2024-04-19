package com.knight.utils;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.experimental.UtilityClass;
import org.springframework.http.HttpStatus;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import java.io.IOException;
import java.io.PrintWriter;
import java.nio.charset.StandardCharsets;

/**
 * 客户端工具类
 *
 * @author lixiao
 */
@UtilityClass
public class ServletUtils {

	/**
	 * 获取当前上下文中的 HttpServletResponse
	 * @return ServletRequestAttributes
	 */
	public HttpServletRequest getRequest() {
		return getRequestAttributes().getRequest();
	}

	/**
	 * 获取当前上下文中的 HttpServletResponse
	 * @return HttpServletResponse
	 */
	public HttpServletResponse getResponse() {
		return getRequestAttributes().getResponse();
	}

	/**
	 * 获取当前上下文中的 ServletRequestAttributes
	 * @return ServletRequestAttributes
	 */
	public ServletRequestAttributes getRequestAttributes() {
		RequestAttributes attributes = RequestContextHolder.getRequestAttributes();
		return (ServletRequestAttributes) attributes;
	}

	/**
	 * 将字符串渲染到客户端
	 * @param response 渲染对象
	 * @param string 待渲染的字符串
	 */
	public static void renderString(HttpServletResponse response, String string) {
		response.setStatus(HttpStatus.OK.value());
		response.setCharacterEncoding(StandardCharsets.UTF_8.name());
		response.setContentType("application/json;charset=UTF-8");
		try (PrintWriter writer = response.getWriter()) {
			writer.print(string);
		}
		catch (IOException e) {
			// ignore
		}
	}

}
