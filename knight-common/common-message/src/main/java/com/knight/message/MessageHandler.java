package com.knight.message;

/**
 * 消息处理程序
 *
 * @author knight
 */
@FunctionalInterface
public interface MessageHandler {

	void handleMessage(Message<?> message) throws MessagingException;

}
