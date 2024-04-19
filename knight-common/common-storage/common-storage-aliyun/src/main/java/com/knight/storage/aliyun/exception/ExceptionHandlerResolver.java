package com.knight.storage.aliyun.exception;

import com.aliyun.oss.ClientException;
import com.aliyun.oss.ServiceException;
import com.knight.entity.base.R;
import com.knight.storage.response.StorageResultConstants;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

/**
 * 异常处理程序解析器
 *
 * @author knight
 * @since 2023/07/03
 */
@Slf4j
@RestControllerAdvice
public class ExceptionHandlerResolver {

	@ExceptionHandler(ServiceException.class)
	@ResponseStatus(code = HttpStatus.OK)
	public R<Void> handleServiceException(ServiceException e) {
		log.error("ExceptionHandlerResolver#handleServiceException: {}", e.getLocalizedMessage());
		return R.failed(StorageResultConstants.OSS_EXCEPTION);
	}

	@ExceptionHandler(ClientException.class)
	@ResponseStatus(code = HttpStatus.OK)
	public R<Void> handleClientException(ClientException e) {
		log.error("ExceptionHandlerResolver#ClientException: {}", e.getLocalizedMessage());
		return R.failed(StorageResultConstants.OSS_EXCEPTION);
	}

}
