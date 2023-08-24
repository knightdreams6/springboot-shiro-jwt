package com.knight.shiro.token;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.apache.shiro.authc.AuthenticationToken;

/**
 * 邮箱验证码登录token
 *
 * @author knight
 */
@Getter
@Setter
@NoArgsConstructor
public class MailCodeToken implements AuthenticationToken {

	/**
	 * 邮箱
	 */
	private String mail;

	/**
	 * 验证码 为什么使用char[]? 参考: <a href=
	 * "https://docs.oracle.com/javase/1.5.0/docs/guide/security/jce/JCERefGuide.html#PBEEx">...</a>
	 */
	private char[] code;

	public MailCodeToken(String mail, String code) {
		this.mail = mail;
		this.code = code.toCharArray();
	}

	@Override
	public Object getPrincipal() {
		return getMail();
	}

	@Override
	public Object getCredentials() {
		return getCode();
	}

}
