package com.knight.message.support;

import com.knight.message.GenericMessage;
import com.knight.message.Message;
import org.springframework.lang.NonNull;
import org.springframework.lang.Nullable;
import org.springframework.util.Assert;

/**
 * 消息生成器
 *
 * @author knight
 */
public class MessageBuilder<T> {

	private final T payload;

	@Nullable
	private final Message<T> providedMessage;

	private MessageBuilder(@NonNull Message<T> providedMessage) {
		Assert.notNull(providedMessage, "Message must not be null");
		this.payload = providedMessage.getPayload();
		this.providedMessage = providedMessage;
	}

	private MessageBuilder(T payload) {
		Assert.notNull(payload, "Payload must not be null");
		this.payload = payload;
		this.providedMessage = null;
	}

	public Message<T> build() {
		if (this.providedMessage != null) {
			return this.providedMessage;
		}
		return new GenericMessage<>(this.payload);
	}

	public static <T> MessageBuilder<T> fromMessage(Message<T> message) {
		return new MessageBuilder<>(message);
	}

	public static <T> MessageBuilder<T> withPayload(T payload) {
		return new MessageBuilder<>(payload);
	}

}
