package com.knight.api.version.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ser.impl.SimpleFilterProvider;
import com.knight.api.version.component.ApiVersionHandlerMapping;
import com.knight.api.version.component.jackson.VersionFieldFilter;
import com.knight.api.version.component.jackson.VersionFieldMixin;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.jackson.Jackson2ObjectMapperBuilderCustomizer;
import org.springframework.boot.autoconfigure.web.servlet.WebMvcRegistrations;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerMapping;

/**
 * api版本配置类
 *
 * @author knight
 * @since 2023/12/06
 */
@Configuration(proxyBeanMethods = false)
public class ApiVersionConfiguration implements WebMvcRegistrations {

	@Override
	public RequestMappingHandlerMapping getRequestMappingHandlerMapping() {
		return new ApiVersionHandlerMapping();
	}

	@Bean
	@ConditionalOnClass(ObjectMapper.class)
	public Jackson2ObjectMapperBuilderCustomizer customizer() {
		return builder -> {
			SimpleFilterProvider simpleFilterProvider = new SimpleFilterProvider();
			simpleFilterProvider.addFilter(VersionFieldMixin.VERSION_FIELD_FILTER, new VersionFieldFilter());
			builder.mixIn(Object.class, VersionFieldMixin.class);
			builder.filters(simpleFilterProvider);
		};
	}

}
