package com.learn.project.common.utils;

import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.crypto.hash.Sha256Hash;

import java.util.concurrent.ThreadLocalRandom;

/**
 * @author lixiao
 * @date 2019/7/31 17:11
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
		return String.valueOf(new Sha256Hash(password, salt, 1024));
	}

}
