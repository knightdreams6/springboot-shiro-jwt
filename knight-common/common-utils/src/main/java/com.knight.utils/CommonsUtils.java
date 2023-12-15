package com.knight.utils;

import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.crypto.hash.Sha256Hash;

import java.util.concurrent.ThreadLocalRandom;

/**
 * @author lixiao
 * @since 2019/7/31 17:11
 */
@Slf4j
public class CommonsUtils {

	/**
	 * 获取六位数验证码
	 * @return 验证码
	 */
	public static int getCode() {
		return ThreadLocalRandom.current().nextInt(100000, 999999);
	}

	/**
	 * 验证码加密
	 * @param code 验证码
	 * @param salt 盐
	 * @return {@link String}
	 */
	public static String encryptCode(String code, String salt) {
		return new Sha256Hash(code, salt).toString();
	}

}
