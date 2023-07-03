package com.knight.storage.minio.exception;

import com.knight.entity.base.R;
import com.knight.storage.response.StorageResultConstants;
import io.minio.errors.MinioException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.net.ConnectException;

/**
 * 异常处理程序解析器
 *
 * @author knight
 * @since 2023/07/03
 */
@Slf4j
@RestControllerAdvice
public class ExceptionHandlerResolver {

	@ExceptionHandler(MinioException.class)
	@ResponseStatus(code = HttpStatus.OK)
	public R<Object> handleMinioException(MinioException e) {
		log.error("ExceptionHandlerResolver#handleMinioException: {}", e.getLocalizedMessage());
		return R.failed(StorageResultConstants.OSS_EXCEPTION);
	}

	@ExceptionHandler(ConnectException.class)
	@ResponseStatus(code = HttpStatus.OK)
	public R<Object> handleConnectException(ConnectException e) {
		log.error("ExceptionHandlerResolver#handleConnectException: {}", e.getLocalizedMessage());
		return R.failed(StorageResultConstants.OSS_EXCEPTION);
	}

}
