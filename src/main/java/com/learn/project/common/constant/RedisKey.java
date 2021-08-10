package com.learn.project.common.constant;

import com.baomidou.mybatisplus.core.toolkit.StringPool;

/**
 * @author lixiao
 * @date 2019/10/26 12:03
 */
public class RedisKey {

	public static String getLoginCodeKey(String phone) {
		return "LOGIN:CODE:" + phone;
	}

	public static String getModifyPasswordCodeKey(String phone) {
		return "MODIFY:PWD:CODE:" + phone;
	}

	public static String getLoginUserKey(String phone) {
		return "LOGIN:USER:" + phone;
	}

	public static String getRequestLimitKey(String servletPath, String phone) {
		return "LIMIT:" + servletPath + StringPool.COLON + phone;
	}

}
