package com.learn.project.framework.web.exception;

import com.learn.project.common.enums.ErrorState;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * @author lixiao
 * @version 1.0
 * @date 2020/4/30 16:31
 */
@Getter
@Setter
@NoArgsConstructor
public class ServiceException extends RuntimeException {

	private ErrorState errorState;

	public ServiceException(ErrorState errorState) {
		this.errorState = errorState;
	}

}
