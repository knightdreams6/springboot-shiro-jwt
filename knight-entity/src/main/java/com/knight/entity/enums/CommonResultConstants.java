package com.knight.entity.enums;

import lombok.Getter;

/**
 * @author lixiao
 * @since 2019/8/5 17:16
 */
@Getter
public enum CommonResultConstants implements IResultConstants {

	/**
	 * 成功
	 */
	SUCCESS(200, "成功", "success"),

	/**
	 * 失败
	 */
	FAIL(500, "失败", "fail"),

	/**
	 * 无数据 代表单个
	 */
	EMPTY_DATA(-1, "无数据", "Empty Data"),

	/**
	 * 无数据 列表
	 */
	EMPTY_LIST(-2, "无数据", "Empty Data"),

	/**
	 * 用户名已存在
	 */
	USER_ALREADY_EXIST(1001, "用户名已存在", "username already exists"),

	/**
	 * 验证码无效
	 */
	CODE_EXPIRE(1002, "验证码无效", "invalid verification code"),

	/**
	 * 验证码不正确
	 */
	CODE_ERROR(1003, "验证码不正确", "incorrect verification code"),

	/**
	 * 用户名不存在
	 */
	USERNAME_NOT_EXIST(1004, "用户名或密码错误", "用户名不存在", "wrong username or password"),

	/**
	 * 密码错误
	 */
	PASSWORD_ERROR(1005, "用户名或密码错误", "密码错误", "wrong username or password"),

	/**
	 * 没有相关权限
	 */
	NOT_AUTH(1006, "没有相关权限", "no relevant permissions"),

	/**
	 * 令牌无效
	 */
	ACCESS_TOKEN_INVALID(1007, "无效的访问令牌", "invalid access token"),

	/**
	 * 缺少相应参数
	 */
	MISSING_PARAMETER(1008, "参数绑定失败:缺少参数", "parameter binding failed: missing parameter"),

	/**
	 * 接口请求限制
	 */
	REQUEST_LIMIT(1009, "请求频繁,请稍后重试", "frequent requests,please try again later"),

	/**
	 * 刷新令牌无效
	 */
	REFRESH_TOKEN_INVALID(1010, "验证已过期,请重新登录", "authentication expired, please log in again"),

	/**
	 * 非法参数异常
	 */
	ILLEGAL_PARAM_EXCEPTION(1011, "非法参数异常", "Illegal parameter exception"),

	/**
	 * 用户名不存在
	 */
	USERNAME_CODE_NOT_EXIST(1012, "用户名或验证码错误", "用户名不存在", "wrong username or code"),

	;

	private final Integer code;

	private final String msg;

	private String msgReally;

	private String enMsg;

	CommonResultConstants(Integer code, String msg) {
		this.code = code;
		this.msg = msg;
	}

	CommonResultConstants(Integer code, String msg, String enMsg) {
		this(code, msg);
		this.enMsg = enMsg;
	}

	CommonResultConstants(Integer code, String msg, String msgReally, String enMsg) {
		this(code, msg, enMsg);
		this.msgReally = msgReally;
	}

	@Override
	public Integer code() {
		return getCode();
	}

	@Override
	public String msg() {
		return getMsg();
	}

	@Override
	public String msgReally() {
		return getMsgReally();
	}

	@Override
	public String enMsg() {
		return getEnMsg();
	}

}
