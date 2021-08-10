package com.learn.project.framework.web.exception;

import cn.hutool.json.JSONUtil;
import com.learn.project.common.enums.ErrorState;
import com.learn.project.framework.web.domain.Result;
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

	private String message;

	public ServiceException(ErrorState errorState) {
		this.message = JSONUtil.toJsonStr(Result.error(errorState));
	}

}
