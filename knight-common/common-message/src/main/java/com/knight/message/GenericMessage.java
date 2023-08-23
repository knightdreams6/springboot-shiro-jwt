package com.knight.message;

import lombok.Data;

import java.io.Serializable;

/**
 * 通用消息
 *
 * @author knight
 */
@Data
public class GenericMessage<T> implements Message<T>, Serializable {

	private final T payload;

	@Override
	public T getPayload() {
		return payload;
	}

}
