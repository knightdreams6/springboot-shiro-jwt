package com.knight.entity.enums;

import lombok.Getter;

/**
 * @author lixiao
 * @date 2019/8/5 17:16 本项目异常使用四位数 第三方接口异常使用五位数
 */
@Getter
public enum ErrorState {

	/** 成功 */
	SUCCESS(200, "成功", "success"),

	/** 失败 */
	FAIL(500, "失败", "fail"),

	/** 短信发送失败 */
	SEND_SMS_ERROR(10001, "短信发送失败", "message failed to send"),

	/** 用户名已存在 */
	USER_ALREADY_EXIST(1001, "用户名已存在", "username already exists"),

	/** 验证码无效 */
	CODE_EXPIRE(1002, "验证码无效", "invalid verification code"),

	/** 验证码不正确 */
	CODE_ERROR(1003, "验证码不正确", "incorrect verification code"),

	/** 用户名不存在 */
	USERNAME_NOT_EXIST(1004, "用户名或密码错误", "wrong username or password"),

	/** 密码错误 *///
	PASSWORD_ERROR(1005, "密码不正确", "wrong password"),

	/** 没有相关权限 */
	NOT_AUTH(1006, "没有相关权限", "no relevant permissions"),

	/** 令牌无效 */
	ACCESS_TOKEN_INVALID(1007, "无效的访问令牌", "invalid access token"),

	/** 缺少相应参数 */
	MISSING_PARAMETER(1008, "参数绑定失败:缺少参数", "parameter binding failed: missing parameter"),

	/** 接口请求限制 */
	REQUEST_LIMIT(1009, "请求频繁,请稍后重试", "frequent requests,please try again later"),

	/** 刷新令牌无效 */
	REFRESH_TOKEN_INVALID(1010, "验证已过期,请重新登录", "authentication expired, please log in again"),

	/** 非法参数异常 */
	ILLEGAL_PARAM_EXCEPTION(1011, "非法参数异常", "Illegal parameter exception"),

	;

	ErrorState(Integer code, String msg) {
		this.code = code;
		this.msg = msg;
	}

	ErrorState(Integer code, String msg, String enMsg) {
		this.code = code;
		this.msg = msg;
		this.enMsg = enMsg;
	}

	private final Integer code;

	private final String msg;

	private String enMsg;

}
