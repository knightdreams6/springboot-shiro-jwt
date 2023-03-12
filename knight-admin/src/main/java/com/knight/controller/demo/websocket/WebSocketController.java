package com.knight.controller.demo.websocket;

import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Controller;

/**
 * websocket控制器
 *
 * @author knight
 * @since 2023/03/12
 */
@Slf4j
@Controller
public class WebSocketController {

	@MessageMapping("/incoming")
	@SendTo("/topic/outgoing")
	public String incoming(Message message) {
		log.info(String.format("received message: %s", message));
		return String.format("Application responded to your message: \"%s\"", message.getMessage());
	}

}
