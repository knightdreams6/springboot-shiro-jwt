package com.knight.message;

import org.springframework.core.NestedRuntimeException;

/**
 * 消息异常
 *
 * @author knight
 */
public class MessagingException extends NestedRuntimeException {

	public MessagingException(String msg) {
		super(msg);
	}

}
