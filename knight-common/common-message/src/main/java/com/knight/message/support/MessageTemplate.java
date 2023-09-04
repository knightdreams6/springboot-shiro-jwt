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
	 * 发送消息(同步)
	 * @param message 消息
	 */
	public void send(Message<?> message) {
		for (MessageHandler messageHandler : messageHandlerList) {
			messageHandler.handleMessage(message, false);
		}
	}

	/**
	 * 发送消息(异步)
	 * @param message 消息
	 */
	public void sendAsync(Message<?> message) {
		for (MessageHandler messageHandler : messageHandlerList) {
			messageHandler.handleMessage(message, true);
		}
	}

}
