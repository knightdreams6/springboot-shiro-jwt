package com.knight.utils;

import lombok.experimental.UtilityClass;
import lombok.extern.slf4j.Slf4j;

/**
 * @author lixiao
 * @date 2019/7/31 17:11
 */
@Slf4j
@UtilityClass
public class OauthUtils {

	final String AUTHORIZATION_HEADER = "Authorization";

	/**
	 * 获取令牌
	 * @return {@link String}
	 */
	public static String getToken() {
		return getToken(ServletUtils.getRequest().getHeader(AUTHORIZATION_HEADER));
	}

	/**
	 * 获取令牌
	 * @param authorizationHeader 授权头
	 * @return {@link String}
	 */
	public static String getToken(String authorizationHeader) {
		if (authorizationHeader == null) {
			return null;
		}
		String[] authTokens = authorizationHeader.split(" ");
		if (authTokens == null || authTokens.length < 2) {
			return null;
		}
		return authTokens[1];
	}

}