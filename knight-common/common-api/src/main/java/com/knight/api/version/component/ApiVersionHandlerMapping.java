package com.knight.api.version.component;

import com.knight.api.version.annotation.ApiVersion;
import org.springframework.core.annotation.AnnotatedElementUtils;
import org.springframework.lang.NonNull;
import org.springframework.lang.Nullable;
import org.springframework.web.servlet.mvc.method.RequestMappingInfo;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerMapping;

import java.lang.reflect.Method;
import java.util.regex.Pattern;

/**
 * ApiVersionHandlerMapping
 *
 * @author knight
 * @since 2023/12/07
 */
public class ApiVersionHandlerMapping extends RequestMappingHandlerMapping {

	public static final Pattern API_VERSION_PATTERN = Pattern.compile("v(\\d+)");

	/**
	 * 使用方法和类型级 @RequestMapping 创建 RequestMappingInfo。
	 */
	@Override
	@Nullable
	protected RequestMappingInfo getMappingForMethod(@NonNull Method method, @NonNull Class<?> handlerType) {
		// 类上的请求 + 方法上的请求
		RequestMappingInfo requestMappingInfo = super.getMappingForMethod(method, handlerType);
		if (requestMappingInfo == null) {
			return null;
		}
		// 尝试方法上/类上获取ApiVersion注解
		ApiVersion apiVersion = AnnotatedElementUtils.findMergedAnnotation(method, ApiVersion.class);
		if (apiVersion == null) {
			apiVersion = AnnotatedElementUtils.findMergedAnnotation(handlerType, ApiVersion.class);
		}
		if (apiVersion == null) {
			return requestMappingInfo;
		}

		String prefix = "v" + apiVersion.value();

		String[] paths = requestMappingInfo.getPatternValues()
			.stream()
			.map(path -> prefix + path)
			.toArray(String[]::new);

		// 组合RequestMappingInfo 添加版本前缀
		return requestMappingInfo.mutate().paths(paths).build();
	}

}
