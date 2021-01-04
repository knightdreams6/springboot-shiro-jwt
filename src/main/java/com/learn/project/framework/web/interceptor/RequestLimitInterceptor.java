package com.learn.project.framework.web.interceptor;

import com.alibaba.fastjson.JSON;
import com.learn.project.common.annotction.RequestLimit;
import com.learn.project.common.constant.RedisKey;
import com.learn.project.common.utils.ServletUtils;
import com.learn.project.common.utils.redis.RedisCache;
import com.learn.project.framework.web.domain.Result;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.lang.reflect.Method;
import java.util.concurrent.TimeUnit;

/**
 * @author lixiao
 * @version 1.0
 * @date 2020/6/22 9:30
 * 请求限制拦截器
 */
@Slf4j
public class RequestLimitInterceptor extends HandlerInterceptorAdapter {

    @Resource
    private RedisCache redisCache;

    /**
     * isAssignableFrom() 判定此 Class 对象所表示的类或接口与指定的 Class 参数所表示的类或接口是否相同，或是否是其超类或超接口
     * isAssignableFrom()方法是判断是否为某个类的父类
     * instanceof关键字是判断是否某个类的子类
     */
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        log.info("接口请求限制拦截器执行了...");
        if (handler.getClass().isAssignableFrom(HandlerMethod.class)) {
            // HandlerMethod 封装方法定义相关的信息,如类,方法,参数等
            HandlerMethod handlerMethod = (HandlerMethod) handler;
            Method method = handlerMethod.getMethod();
            // 获取方法中是否包含注解
            RequestLimit methodAnnotation = method.getAnnotation(RequestLimit.class);
            // 获取类中是否包含注解
            RequestLimit classAnnotation = method.getDeclaringClass().getAnnotation(RequestLimit.class);
            // 如果方法上有注解就优先使用方法上的注解的参数，否则使用类上的
            RequestLimit requestLimit = methodAnnotation != null ? methodAnnotation : classAnnotation;
            if (requestLimit != null) {
                if (isLimit(request, requestLimit)) {
                    // 返回请求限制错误
                    ServletUtils.renderString(response, JSON.toJSONString(Result.error(requestLimit.msg())));
                    return false;
                }
            }
        }
        return super.preHandle(request, response, handler);
    }


    /**
     * 判断是否超过次数限制
     *
     * @param request      HttpServletRequest
     * @param requestLimit requestLimit
     * @return boolean true表示超过
     */
    public boolean isLimit(HttpServletRequest request, RequestLimit requestLimit) {
        String limitKey = RedisKey.getRequestLimitKey(request.getServletPath(), request.getParameter("phone"));
        Integer count = redisCache.getCacheObject(limitKey);
        if (count == null) {
            // 初始化次数
            redisCache.setCacheObject(limitKey, 1, requestLimit.second(), TimeUnit.SECONDS);
        } else {
            if (count >= requestLimit.maxCount()) {
                return true;
            }
            // 次数自增
            redisCache.setCacheObject(limitKey, count + 1);
        }
        return false;
    }
}
