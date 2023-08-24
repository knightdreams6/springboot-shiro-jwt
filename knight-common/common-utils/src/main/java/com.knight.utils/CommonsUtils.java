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
	 * 使用SHA256加密
	 * @param password 需要加密的密码
	 * @param salt 盐
	 * @return 返回加密后的密码
	 */
	public static String encryptPassword(String password, String salt) {
		return new Sha256Hash(password, salt, 1024).toHex();
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
