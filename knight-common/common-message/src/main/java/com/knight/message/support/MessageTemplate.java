package com.knight.message.support;

import com.knight.message.Message;
import com.knight.message.MessageHandler;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * 消息模板
 *
 * @author knight
 */
@AllArgsConstructor
@Component
public class MessageTemplate {

	/**
	 * 消息处理程序
	 */
	private final List<MessageHandler> messageHandlerList;

	/**
	 * 发送消息
	 * @param message 消息
	 */
	public void send(Message<?> message) {
		for (MessageHandler messageHandler : messageHandlerList) {
			messageHandler.handleMessage(message);
		}
	}

}
