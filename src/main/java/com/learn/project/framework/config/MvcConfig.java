package com.learn.project.framework.config;

import com.learn.project.framework.interceptor.RequestLimitInterceptor;
import com.learn.project.framework.interceptor.impl.SameUrlDataInterceptor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * @author lixiao
 * @date 2020/1/14 15:38
 */
@Configuration
public class MvcConfig implements WebMvcConfigurer {

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**")
                //设置允许跨域请求的域名
                .allowedOrigins("*")
                .allowCredentials(false)
                .allowedMethods("*")
                .maxAge(3600);
    }


    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(requestLimitInterceptor())
                .excludePathPatterns("/webjars/**", "/swagger-ui.html", "/swagger-resources/**", "/v2/**", "/images/**")
                .addPathPatterns("/**")
                .order(Integer.MAX_VALUE-1);

        registry.addInterceptor(new SameUrlDataInterceptor())
                .excludePathPatterns("/login/**", "/swagger-ui.html", "/swagger-resources/**", "/v2/**", "/images/**")
                .addPathPatterns("/**")
                .order(Integer.MAX_VALUE);
    }

    @Bean
    public RequestLimitInterceptor requestLimitInterceptor(){
        return new RequestLimitInterceptor();
    }
}
