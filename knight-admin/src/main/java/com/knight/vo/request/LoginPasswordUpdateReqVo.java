package com.knight.vo.request;

import com.knight.valid.annotation.PhoneNumber;
import jakarta.validation.constraints.NotEmpty;
import lombok.Data;

/**
 * 登录密码更新请求参数vo
 *
 * @author knight
 * @since 2023/07/12
 */
@Data
public class LoginPasswordUpdateReqVo {

	/**
	 * 用户名
	 */
	@PhoneNumber
	@NotEmpty(message = "用户名不能为空")
	private String username;

	/**
	 * 验证码
	 */
	@NotEmpty(message = "验证码不能为空")
	private String code;

	/**
	 * 密码
	 */
	@NotEmpty(message = "密码不能为空")
	private String password;

}
