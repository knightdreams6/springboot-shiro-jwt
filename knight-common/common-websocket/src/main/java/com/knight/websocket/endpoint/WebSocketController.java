package com.knight.websocket.endpoint;

import com.knight.entity.base.R;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.authz.annotation.RequiresRoles;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.messaging.simp.user.SimpUser;
import org.springframework.messaging.simp.user.SimpUserRegistry;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.security.Principal;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * websocket控制器
 *
 * @author knight
 * @since 2023/03/12
 */
@Slf4j
@Controller
@RequiredArgsConstructor
public class WebSocketController {

	/**
	 * Simp消息模板
	 */
	private final SimpMessagingTemplate simpMessagingTemplate;

	/**
	 * websocket用户注册表
	 */
	private final SimpUserRegistry simpUserRegistry;

	@ApiOperation(value = "获取连接的用户列表")
	@RequiresRoles("admin")
	@GetMapping("/ws/user/list")
	@ResponseBody
	public R<Set<Principal>> websocketUsers() {
		return R.ok(simpUserRegistry.getUsers().stream().map(SimpUser::getPrincipal).collect(Collectors.toSet()));
	}

	@MessageMapping("/incoming")
	@SendTo("/topic/outgoing")
	public String incoming(Message message) {
		log.info(String.format("received message: %s", message));
		return String.format("Application responded to your message: \"%s\"", message.getMessage());
	}

	@ApiOperation(value = "给指定的用户发送消息")
	@GetMapping("/ws/send")
	@ResponseBody
	public R<Object> sendUser(@RequestParam String user, @RequestParam String payload) {
		simpMessagingTemplate.convertAndSendToUser(user, "/queue/specified", payload);
		return R.ok();
	}

}
