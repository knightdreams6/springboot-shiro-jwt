package com.learn.project.framework.web.exception;

import com.learn.project.common.enums.ErrorState;
import com.learn.project.framework.web.domain.Result;
import org.apache.shiro.authc.IncorrectCredentialsException;
import org.apache.shiro.authz.AuthorizationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import javax.validation.ConstraintViolationException;

/**
 * @author lixiao
 * @date 2019/8/7 11:09
 */
@RestControllerAdvice
public class GlobalExceptionAdvice {

	/**
	 * 非法参数异常
	 * @see org.springframework.util.Assert
	 * @param e e
	 * @return ResponseEntity
	 */
	@ExceptionHandler(IllegalArgumentException.class)
	public ResponseEntity<Result> handleIllegalArgumentException(IllegalArgumentException e) {
		return ResponseEntity.status(HttpStatus.OK).contentType(MediaType.APPLICATION_JSON)
				.body(Result.error(e.getMessage()));
	}

	/**
	 * 通用业务异常
	 * @param e e
	 * @return ResponseEntity
	 */
	@ExceptionHandler(ServiceException.class)
	public ResponseEntity<Result> handleServiceException(ServiceException e) {
		return ResponseEntity.status(HttpStatus.OK).contentType(MediaType.APPLICATION_JSON)
				.body(Result.error(e.getErrorState()));
	}

	/**
	 * 参数校验异常
	 * @param e e
	 * @return ResponseEntity
	 */
	@ExceptionHandler(ConstraintViolationException.class)
	public ResponseEntity<Result> handleConstraintViolationException(ConstraintViolationException e) {
		return ResponseEntity.status(HttpStatus.OK).contentType(MediaType.APPLICATION_JSON)
				.body(Result.error(e.getMessage()));
	}

	/**
	 * shiro权限异常处理
	 * @return ResponseEntity
	 */
	@ExceptionHandler(AuthorizationException.class)
	public ResponseEntity<Result> handleShiroException() {
		return ResponseEntity.status(HttpStatus.UNAUTHORIZED).contentType(MediaType.APPLICATION_JSON)
				.body(Result.error(ErrorState.NOT_AUTH));
	}

	/**
	 * token无效异常
	 */
	@ExceptionHandler(IncorrectCredentialsException.class)
	public ResponseEntity<Result> handleTokenException() {
		return ResponseEntity.status(HttpStatus.UNAUTHORIZED).contentType(MediaType.APPLICATION_JSON)
				.body(Result.error(ErrorState.TOKEN_INVALID));
	}

	/**
	 * 参数校验(缺少)异常处理
	 * @return ResponseEntity
	 */
	@ExceptionHandler(MissingServletRequestParameterException.class)
	public ResponseEntity<Result> handleMissingParameterException() {
		return ResponseEntity.status(HttpStatus.BAD_REQUEST).contentType(MediaType.APPLICATION_JSON)
				.body(Result.error(ErrorState.MISSING_PARAMETER));
	}

	/**
	 * 处理运行时异常
	 * @return {@link ResponseEntity<String>}
	 */
	@ExceptionHandler(RuntimeException.class)
	public ResponseEntity<Result> handleRuntimeException() {
		return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).contentType(MediaType.APPLICATION_JSON)
				.body(Result.error("服务器异常"));
	}

}
