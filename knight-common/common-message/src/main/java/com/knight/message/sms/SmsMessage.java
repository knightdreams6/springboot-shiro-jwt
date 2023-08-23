package com.knight.message.sms;

import lombok.Data;

import java.util.Set;

/**
 * 短信消息
 *
 * @author knight
 */
@Data
public class SmsMessage {

	/**
	 * 接收者
	 */
	private Set<String> receiver;

	/**
	 * 模板代码
	 */
	private String templateCode;

	/**
	 * 模板参数
	 */
	private String templateParam;

}
