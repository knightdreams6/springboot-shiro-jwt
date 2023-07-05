package com.knight.websocket.interceptor;

import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import com.knight.utils.OauthUtils;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.BearerToken;
import org.apache.shiro.mgt.SecurityManager;
import org.apache.shiro.subject.Subject;
import org.apache.shiro.util.ThreadContext;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.simp.stomp.StompCommand;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.messaging.support.ChannelInterceptor;
import org.springframework.messaging.support.MessageHeaderAccessor;

/**
 * 连接授权拦截器
 *
 * @see <a href=
 * "https://docs.spring.io/spring-framework/reference/web/websocket/stomp/authentication-token-based.html">...</a>
 * @author knight
 * @since 2023/03/12
 */
@RequiredArgsConstructor
public class ConnectAuthorizationInterceptor implements ChannelInterceptor {

	/**
	 * 安全管理器
	 */
	private final SecurityManager securityManager;

	@Override
	public Message<?> preSend(@NonNull Message<?> message, @NonNull MessageChannel channel) {
		StompHeaderAccessor accessor = MessageHeaderAccessor.getAccessor(message, StompHeaderAccessor.class);
		// 建立连接时进行身份认证
		if (ObjectUtil.isNotNull(accessor) && ObjectUtil.equals(StompCommand.CONNECT, accessor.getCommand())) {
			// 获取授权请求头
			String authorizationHeader = accessor.getFirstNativeHeader(OauthUtils.AUTHORIZATION_HEADER);

			if (StrUtil.isBlank(authorizationHeader)) {
				return null;
			}

			// 绑定securityManager至当前线程
			ThreadContext.bind(securityManager);
			// 授权
			try {
				Subject subject = SecurityUtils.getSubject();
				subject.login(new BearerToken(OauthUtils.getToken(authorizationHeader)));
				accessor.setUser(() -> subject.getPrincipal().toString());
			}
			catch (Exception e) {
				return null;
			}
		}
		return message;
	}

	@Override
	public void afterSendCompletion(@NonNull Message<?> message, @NonNull MessageChannel channel, boolean sent,
			Exception ex) {
		ThreadContext.unbindSecurityManager();
		ThreadContext.unbindSubject();
	}

}
