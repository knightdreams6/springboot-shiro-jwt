package com.knight.swagger.config;

import com.knight.swagger.handler.OpenApiHandler;
import io.swagger.v3.oas.models.ExternalDocumentation;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import org.springdoc.core.customizers.OpenApiBuilderCustomizer;
import org.springdoc.core.customizers.ServerBaseUrlCustomizer;
import org.springdoc.core.properties.SpringDocConfigProperties;
import org.springdoc.core.providers.SpringDocJavadocProvider;
import org.springdoc.core.service.OpenAPIService;
import org.springdoc.core.service.SecurityService;
import org.springdoc.core.utils.PropertyResolverUtils;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;
import java.util.Optional;

/**
 * swagger文档配置
 *
 * @author knight
 */
@Configuration(proxyBeanMethods = false)
public class SpringDocConfig {

	private Info info() {
		return new Info().title("SpringBoot+Shiro+Jwt前后端分离脚手架")
			.description("")
			.contact(new Contact().email("knightdreams6@163.com"));
	}

	@Bean
	public OpenAPI openAPI() {
		return new OpenAPI().info(info()).externalDocs(new ExternalDocumentation());
	}

	/**
	 * 自定义 openapi 处理器
	 */
	@Bean
	public OpenAPIService openApiBuilder(Optional<OpenAPI> openAPI, SecurityService securityParser,
			SpringDocConfigProperties springDocConfigProperties, PropertyResolverUtils propertyResolverUtils,
			Optional<List<OpenApiBuilderCustomizer>> openApiBuilderCustomisers,
			Optional<List<ServerBaseUrlCustomizer>> serverBaseUrlCustomisers) {
		SpringDocJavadocProvider javadocProvider = new SpringDocJavadocProvider();
		return new OpenApiHandler(openAPI, securityParser, springDocConfigProperties, propertyResolverUtils,
				openApiBuilderCustomisers, serverBaseUrlCustomisers, Optional.of(javadocProvider));
	}

}
