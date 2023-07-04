package com.knight.swagger.config;

import io.swagger.annotations.ApiOperation;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

/**
 * @author lixiao
 * @since 2019/12/17 17:16
 */
@Configuration
@EnableSwagger2
public class SwaggerConfig {

	@Bean
	public Docket createRestApi() {
		return new Docket(DocumentationType.SWAGGER_2)
			.apiInfo(new ApiInfoBuilder().title("SpringBoot+Shiro+Jwt前后端分离脚手架")
				.description("接口文档")
				.termsOfServiceUrl("")
				.version("1.0.0")
				.build())
			// 设置哪些接口暴露给Swagger展示
			.select()
			// 指定扫描有Api注解的包
			.apis(RequestHandlerSelectors.withMethodAnnotation(ApiOperation.class))
			// 扫描指定包中的swagger注解
			.paths(PathSelectors.any())
			.build();
	}

}
