package com.knight.websocket.config;

import com.knight.websocket.interceptor.ConnectAuthorizationInterceptor;
import lombok.RequiredArgsConstructor;
import org.apache.shiro.mgt.SecurityManager;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.messaging.simp.config.ChannelRegistration;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;
import org.springframework.web.socket.config.annotation.WebSocketMessageBrokerConfigurer;

/**
 * websocket stomp 配置 官方文档地址 <a href=
 * "https://docs.spring.io/spring-framework/docs/current/reference/html/web.html#websocket">...</a>
 *
 * @author knight
 * @since 2023/03/12
 */
@AutoConfiguration(afterName = { "ThreadPoolConfig" })
@RequiredArgsConstructor
@EnableWebSocketMessageBroker
public class WebSocketStompConfig implements WebSocketMessageBrokerConfigurer {

	/** 线程池任务执行人 */
	private final ThreadPoolTaskExecutor threadPoolTaskExecutor;

	/** 线程池任务调度器 */
	private final ThreadPoolTaskScheduler threadPoolTaskScheduler;

	private final SecurityManager securityManager;

	@Override
	public void registerStompEndpoints(StompEndpointRegistry registry) {
		registry
			// 设置端点
			.addEndpoint("/websocket")
			// 配置跨域
			.setAllowedOriginPatterns("*")
			// 启用 SockJS
			.withSockJS()
			// SockJS 心跳的调度程序
			.setTaskScheduler(threadPoolTaskScheduler);
	}

	@Override
	public void configureMessageBroker(MessageBrokerRegistry config) {
		// 客户端发送消息队列前缀
		config.setApplicationDestinationPrefixes("/app");
		// 客户端订阅/服务端推送地址前缀
		config.enableSimpleBroker("/topic", "/queue");
		// 指定用户发送订阅前缀
		config.setUserDestinationPrefix("/user");
	}

	@Override
	public void configureClientInboundChannel(ChannelRegistration registration) {
		registration
			// 消息入站拦截器
			.interceptors(new ConnectAuthorizationInterceptor(securityManager))
			// 入站消息通道线程池
			.taskExecutor(threadPoolTaskExecutor);
	}

	@Override
	public void configureClientOutboundChannel(ChannelRegistration registration) {
		registration
			// 出站消息通道线程池
			.taskExecutor(threadPoolTaskExecutor);
	}

}
