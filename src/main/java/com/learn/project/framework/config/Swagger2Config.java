package com.learn.project.framework.config;

import com.learn.project.common.constant.Constant;
import com.learn.project.framework.annotction.PhoneNumber;
import io.swagger.annotations.Api;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.ParameterBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.schema.ModelRef;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Parameter;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

import java.util.ArrayList;
import java.util.List;

/**
 * @author lixiao
 * @date 2019/12/17 17:16
 */
@Configuration
@EnableSwagger2
public class Swagger2Config {

    @Bean
    public Docket createRestApi() {
        ParameterBuilder tokenPar = new ParameterBuilder();
        List<Parameter> pars = new ArrayList<>();
        tokenPar.name(Constant.TOKEN_HEADER_NAME).description("令牌").modelRef(new ModelRef("string")).parameterType("header").required(false).build();
        pars.add(tokenPar.build());
        return new Docket(DocumentationType.SWAGGER_2)
                .apiInfo(apiInfo())
                .select()
                //指定扫描有Api注解的包
                .apis(RequestHandlerSelectors.withClassAnnotation(Api.class))
                .paths(PathSelectors.any())
                .build()
                //添加令牌
                .globalOperationParameters(pars)
                .ignoredParameterTypes(PhoneNumber.class)
                ;
    }

    private ApiInfo apiInfo() {
        return new ApiInfoBuilder()
                .title("SpringBoot+Shiro+Jwt前后端分离脚手架")
                .description("接口文档")
                .termsOfServiceUrl("")
                .version("1.0.0")
                .build();
    }
}
