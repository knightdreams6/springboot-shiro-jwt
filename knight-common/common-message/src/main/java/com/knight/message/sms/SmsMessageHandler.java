package com.knight.message.sms;

import com.knight.message.Message;
import com.knight.message.MessageHandler;
import com.knight.message.MessagingException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

/**
 * 短信消息处理
 *
 * @author knight
 */
@Slf4j
@Component
public class SmsMessageHandler implements MessageHandler {

	@Override
	public void handleMessage(Message<?> message, boolean async) throws MessagingException {
		Object payload = message.getPayload();
		if (!(payload instanceof SmsMessage)) {
			return;
		}
		SmsMessage smsMessage = (SmsMessage) payload;
		log.info("send sms {}", smsMessage);
	}

}
