package com.knight.exception;

import com.knight.entity.enums.IResultConstants;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * @author lixiao
 * @version 1.0
 * @since 2020/4/30 16:31
 */
@Getter
@Setter
@NoArgsConstructor
public class ServiceException extends RuntimeException {

	private IResultConstants errorState;

	public ServiceException(IResultConstants errorState) {
		this.errorState = errorState;
	}

}
