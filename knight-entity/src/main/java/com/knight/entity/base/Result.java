package com.knight.entity.base;

import com.knight.entity.enums.ErrorState;
import lombok.Data;

import java.io.Serializable;

/**
 * @author lixiao
 * @date 2019/7/31 14:46
 */
@Data
public class Result implements Serializable {

	private static final long serialVersionUID = 1L;

	/** 状态码 */
	private Integer code;

	/** 消息 */
	private String msg;

	/** 数据 */
	private Object data;

	public Result(Integer code, String msg) {
		this.code = code;
		this.msg = msg;
	}

	public Result(Integer code, String msg, Object data) {
		this.code = code;
		this.msg = msg;
		this.data = data;
	}

	/**
	 * 返回成功消息
	 * @return 成功消息
	 */
	public static Result success() {
		return Result.success(null);
	}

	/**
	 * 返回成功数据
	 * @return 成功消息
	 */
	public static Result success(Object data) {
		ErrorState successState = ErrorState.SUCCESS;
		return new Result(successState.getCode(), successState.getMsg(), data);
	}

	/**
	 * 返回错误消息
	 */
	public static Result error() {
		return Result.error(ErrorState.FAIL);
	}

	/**
	 * 返回错误消息
	 * @param errorState 错误信息枚举类
	 * @return 警告消息
	 */
	public static Result error(ErrorState errorState) {
		return new Result(errorState.getCode(), errorState.getMsg());
	}

}
