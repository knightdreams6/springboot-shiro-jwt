package com.knight.entity.constans;

/**
 * @author lixiao
 * @since 2019/10/26 12:03
 */
public class RedisKey {

	public static String getLoginCodeKey(String phone) {
		return "LOGIN:CODE:" + phone;
	}

	public static String getModifyPasswordCodeKey(String phone) {
		return "MODIFY:PWD:CODE:" + phone;
	}

	public static String getResetPwdVerifyCodeKey(String mail) {
		return "LOGIN:RESET:PWD:" + mail;
	}

}
