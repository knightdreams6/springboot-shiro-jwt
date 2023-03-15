package com.knight.controller.demo.websocket;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;

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

	private final SimpMessagingTemplate simpMessagingTemplate;

	@MessageMapping("/incoming")
	@SendTo("/topic/outgoing")
	public String incoming(Message message) {
		log.info(String.format("received message: %s", message));
		return String.format("Application responded to your message: \"%s\"", message.getMessage());
	}

	@GetMapping("/websocket/send")
	@ResponseBody
	public String sendUser() {
		simpMessagingTemplate.convertAndSendToUser("18735182285", "/queue/specified",
				"Send a message to a specified user");
		return "ok";
	}

}
