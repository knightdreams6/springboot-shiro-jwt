package com.knight;

import cn.hutool.core.util.ClassUtil;
import com.knight.entity.base.Result;
import lombok.AllArgsConstructor;
import lombok.NonNull;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.core.MethodParameter;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseBodyAdvice;

/**
 * i18n响应
 *
 * @author lixiao
 * @date 2022/06/03
 */
@RestControllerAdvice
@AllArgsConstructor
public class I18nResponseAdvice implements ResponseBodyAdvice<Result> {

	/** 消息来源 */
	private final MessageSource messageSource;

	@Override
	public boolean supports(@NonNull MethodParameter returnType,
			@NonNull Class<? extends HttpMessageConverter<?>> converterType) {
		// 校验返回值类型是否为 Result
		return ClassUtil.isAssignable(returnType.getParameterType(), Result.class);
	}

	@Override
	public Result beforeBodyWrite(Result body, @NonNull MethodParameter returnType,
			@NonNull MediaType selectedContentType,
			@NonNull Class<? extends HttpMessageConverter<?>> selectedConverterType, @NonNull ServerHttpRequest request,
			@NonNull ServerHttpResponse response) {
		if (body == null) {
			return null;
		}
		// 获取对应Locale下的message
		String msg = messageSource.getMessage(String.valueOf(body.getCode()), null, LocaleContextHolder.getLocale());
		body.setMsg(msg);
		return body;
	}

}
