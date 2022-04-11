package com.knight.interceptor.impl;

import cn.hutool.core.util.StrUtil;
import cn.hutool.extra.spring.SpringUtil;
import cn.hutool.json.JSONUtil;
import com.knight.entity.base.Result;
import com.knight.interceptor.LimitKeyGenerator;
import com.knight.interceptor.SimpleLimitKeyGenerator;
import com.knight.interceptor.annotation.RequestLimit;
import com.knight.utils.ServletUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.lang.NonNull;
import org.springframework.util.Assert;
import org.springframework.util.function.SingletonSupplier;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.lang.reflect.Method;
import java.util.concurrent.TimeUnit;

/**
 * 限制请求拦截器
 *
 * @author lixiao
 * @version 1.0
 * @date 2020/6/22 9:30
 */
@Slf4j
public class RequestLimitInterceptor implements HandlerInterceptor {

	/** 限制键生成器 */
	private final SingletonSupplier<LimitKeyGenerator> simpleLimitKeyGenerator = SingletonSupplier
			.of(SimpleLimitKeyGenerator::new);

	@Resource(name = "intRedisTemplate")
	private RedisTemplate<String, Integer> redisTemplate;

	/**
	 * isAssignableFrom() 判定此 Class 对象所表示的类或接口与指定的 Class 参数所表示的类或接口是否相同，或是否是其超类或超接口
	 * isAssignableFrom()方法是判断是否为某个类的父类 instanceof关键字是判断是否某个类的子类
	 */
	@Override
	public boolean preHandle(@NonNull HttpServletRequest request, @NonNull HttpServletResponse response,
			Object handler) {
		if (handler.getClass().isAssignableFrom(HandlerMethod.class)) {
			log.info("接口请求限制拦截器执行了...");
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
				if (isLimit(request, handlerMethod, requestLimit)) {
					// 返回请求限制错误
					ServletUtils.renderString(response, JSONUtil.toJsonStr(Result.error(requestLimit.msg())));
					return false;
				}
			}
		}
		return true;
	}

	/**
	 * 判断是否超过次数限制
	 * @param request HttpServletRequest
	 * @param handlerMethod handlerMethod
	 * @param requestLimit requestLimit
	 * @return boolean true表示超过
	 */
	public boolean isLimit(HttpServletRequest request, HandlerMethod handlerMethod, RequestLimit requestLimit) {
		String limitKey;
		// 如果注解指定的key不为空
		if (StrUtil.isNotEmpty(requestLimit.key())) {
			limitKey = requestLimit.key();
		}
		else {
			// 如果指定的keyGenerator不为空
			String customKeyGeneratorName = requestLimit.keyGenerator();
			LimitKeyGenerator limitKeyGenerator;
			if (StrUtil.isNotEmpty(requestLimit.keyGenerator())) {
				limitKeyGenerator = SpringUtil.getBean(customKeyGeneratorName, LimitKeyGenerator.class);
				Assert.notNull(limitKeyGenerator, String.format("指定的limitKey:%s生成器不存在", customKeyGeneratorName));
			}
			else {
				limitKeyGenerator = simpleLimitKeyGenerator.obtain();
			}
			limitKey = limitKeyGenerator.generate(request, handlerMethod);
		}

		Integer count = redisTemplate.opsForValue().get(limitKey);
		if (count == null) {
			// 初始化次数
			redisTemplate.opsForValue().set(limitKey, 1, requestLimit.second(), TimeUnit.SECONDS);
		}
		else {
			if (count >= requestLimit.maxCount()) {
				return true;
			}
			// 次数自增
			redisTemplate.opsForValue().set(limitKey, count + 1, requestLimit.second(), TimeUnit.SECONDS);
		}
		return false;
	}

}
