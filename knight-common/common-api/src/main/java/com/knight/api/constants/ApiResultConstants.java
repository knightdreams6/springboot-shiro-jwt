package com.knight.api.constants;

import com.knight.entity.enums.IResultConstants;
import lombok.Getter;

/**
 * API模块返回值常量
 *
 * @author knight
 * @since 2023/12/14
 */
@Getter
public enum ApiResultConstants implements IResultConstants {

	/**
	 * 当前api需要进行风险认证
	 */
	API_RISK(1, "当前api需要进行风险认证"),

	/**
	 * 未找到api风险认证信息
	 */
	API_RISK_INFO_NOT_FOUND(2, "未找到api风险认证信息"),

	;

	private final Integer code;

	private final String msg;

	private String msgReally;

	private String enMsg;

	ApiResultConstants(Integer code, String msg) {
		this.code = 20000 + code;
		this.msg = msg;
	}

	ApiResultConstants(Integer code, String msg, String enMsg) {
		this(code, msg);
		this.enMsg = enMsg;
	}

	ApiResultConstants(Integer code, String msg, String msgReally, String enMsg) {
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
