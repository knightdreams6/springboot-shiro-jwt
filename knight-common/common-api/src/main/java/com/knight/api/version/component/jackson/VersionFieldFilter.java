package com.knight.api.version.component.jackson;

import com.fasterxml.jackson.databind.ser.PropertyWriter;
import com.fasterxml.jackson.databind.ser.impl.SimpleBeanPropertyFilter;
import com.knight.api.version.annotation.ApiVersionFiled;
import com.knight.api.version.component.ApiVersionHandlerMapping;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import java.util.regex.Matcher;

/**
 * 版本字段过滤
 *
 * @author knight
 * @since 2023/12/06
 */
public class VersionFieldFilter extends SimpleBeanPropertyFilter {

	@Override
	protected boolean include(PropertyWriter writer) {
		ApiVersionFiled versionFiled = writer.getAnnotation(ApiVersionFiled.class);
		if (versionFiled == null) {
			return true;
		}

		// 获取当前接口的版本
		ServletRequestAttributes requestAttributes = (ServletRequestAttributes) RequestContextHolder
			.getRequestAttributes();
		if (requestAttributes == null) {
			return false;
		}
		String requestURI = requestAttributes.getRequest().getRequestURI();
		Matcher matcher = ApiVersionHandlerMapping.API_VERSION_PATTERN.matcher(requestURI);
		if (!matcher.find()) {
			return true;
		}
		Integer currentApiVersion = Integer.valueOf(matcher.group(1));

		// 当前字段版本
		int fieldVersion = versionFiled.value();
		return versionFiled.strategy().apply(currentApiVersion, fieldVersion);
	}

}
