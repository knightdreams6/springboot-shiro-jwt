package com.knight.exception;

import com.knight.entity.base.R;
import com.knight.entity.enums.CommonResultConstants;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.authz.AuthorizationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import javax.validation.ConstraintViolationException;

/**
 * @author lixiao
 * @since 2019/8/7 11:09
 */
@Slf4j
@RestControllerAdvice
public class GlobalExceptionAdvice {

	/**
	 * 非法参数异常
	 * @return ResponseEntity
	 * @see org.springframework.util.Assert
	 */
	@ExceptionHandler(IllegalArgumentException.class)
	public ResponseEntity<R<Object>> handleIllegalArgumentException(IllegalArgumentException e) {
		log.error("IllegalArgumentException: {}", e.getLocalizedMessage());
		return ResponseEntity.status(HttpStatus.OK)
			.contentType(MediaType.APPLICATION_JSON)
			.body(R.failed(CommonResultConstants.ILLEGAL_PARAM_EXCEPTION));
	}

	/**
	 * 通用业务异常
	 * @param e e
	 * @return ResponseEntity
	 */
	@ExceptionHandler(ServiceException.class)
	public ResponseEntity<R<Object>> handleServiceException(ServiceException e) {
		return ResponseEntity.status(HttpStatus.OK)
			.contentType(MediaType.APPLICATION_JSON)
			.body(R.failed(e.getErrorState()));
	}

	/**
	 * 参数校验异常
	 * @param e e
	 * @return ResponseEntity
	 */
	@ExceptionHandler(ConstraintViolationException.class)
	public ResponseEntity<R<Object>> handleConstraintViolationException(ConstraintViolationException e) {
		return ResponseEntity.status(HttpStatus.OK)
			.contentType(MediaType.APPLICATION_JSON)
			.body(R.failed(e.getLocalizedMessage()));
	}

	/**
	 * 参数校验异常
	 * @param e e
	 * @return ResponseEntity
	 */
	@ExceptionHandler(MethodArgumentNotValidException.class)
	public ResponseEntity<R<Object>> handleConstraintViolationException(MethodArgumentNotValidException e) {
		return ResponseEntity.status(HttpStatus.OK)
			.contentType(MediaType.APPLICATION_JSON)
			.body(R.failed(e.getAllErrors().get(0).getDefaultMessage()));
	}

	/**
	 * shiro权限异常处理
	 * @return ResponseEntity
	 */
	@ExceptionHandler(AuthorizationException.class)
	public ResponseEntity<R<Object>> handleShiroException(AuthorizationException e) {
		return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
			.contentType(MediaType.APPLICATION_JSON)
			.body(R.failed(CommonResultConstants.NOT_AUTH));
	}

	/**
	 * 参数校验(缺少)异常处理
	 * @return ResponseEntity
	 */
	@ExceptionHandler(MissingServletRequestParameterException.class)
	public ResponseEntity<R<Object>> handleMissingParameterException() {
		return ResponseEntity.status(HttpStatus.BAD_REQUEST)
			.contentType(MediaType.APPLICATION_JSON)
			.body(R.failed(CommonResultConstants.MISSING_PARAMETER));
	}

}
