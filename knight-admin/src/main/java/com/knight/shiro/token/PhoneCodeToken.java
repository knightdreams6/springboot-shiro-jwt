package com.knight.shiro.token;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.apache.shiro.authc.AuthenticationToken;

/**
 * 手机号验证码登录token
 *
 * @author knight
 * @date 2021/08/03
 */
@Getter
@Setter
@NoArgsConstructor
public class PhoneCodeToken implements AuthenticationToken {

	/**
	 * 手机号
	 */
	private String phone;

	/**
	 * 验证码 为什么使用char[]? 参考: <a href=
	 * "https://docs.oracle.com/javase/1.5.0/docs/guide/security/jce/JCERefGuide.html#PBEEx">...</a>
	 */
	private char[] code;

	public PhoneCodeToken(String phone, String code) {
		this.phone = phone;
		this.code = code.toCharArray();
	}

	@Override
	public Object getPrincipal() {
		return getPhone();
	}

	@Override
	public Object getCredentials() {
		return getCode();
	}

}
