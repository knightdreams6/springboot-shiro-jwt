package com.knight.vo.response;

import lombok.Data;

/**
 * 登录授权数据vo
 *
 * @author knight
 */
@Data
public class LoginAuthDataVo {

	/**
	 * 访问令牌
	 */
	private String token;

	/**
	 * 刷新令牌
	 */
	private String refreshToken;

}
