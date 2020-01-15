package com.learn.project.framework.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * @author lixiao
 * @date 2020/1/14 15:38
 */
@Configuration
public class MvcConfig implements WebMvcConfigurer {

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/user/**")
                //设置允许跨域请求的域名
                .allowedOrigins("*")
                .allowCredentials(false)
                .allowedMethods("*")
                .maxAge(3600);
    }
}
