package com.knight.config;

import com.knight.api.limit.component.ApiLimitInterceptor;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.Ordered;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * @author lixiao
 * @since 2020/1/14 15:38
 */
@Configuration
@RequiredArgsConstructor
public class MvcConfig implements WebMvcConfigurer {

	/**
	 * api限制拦截器
	 */
	private final ApiLimitInterceptor apiLimitInterceptor;

	@Override
	public void addCorsMappings(CorsRegistry registry) {
		registry.addMapping("/**")
			// 设置允许跨域请求的域名
			.allowedOrigins("*")
			.allowCredentials(false)
			.allowedMethods("*")
			.maxAge(3600);
	}

	@Override
	public void addInterceptors(InterceptorRegistry registry) {
		registry.addInterceptor(apiLimitInterceptor).order(Ordered.LOWEST_PRECEDENCE - 1);
	}

}
