package com.knight.vo.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import lombok.Data;

/**
 * 邮箱验证码登录请求参数vo
 *
 * @author knight
 */
@Data
public class LoginMailCodeReqVo {

	/**
	 * 邮箱
	 */
	@Email
	@NotEmpty(message = "邮箱不能为空")
	private String mail;

	/**
	 * 验证码
	 */
	@Size(min = 6, max = 6)
	@NotEmpty(message = "验证码不能为空")
	private String code;

}
