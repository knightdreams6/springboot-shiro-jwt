package com.knight.vo.request;

import jakarta.validation.constraints.NotEmpty;
import lombok.Data;

/**
 * 邮箱重置密码请求参数
 *
 * @author knight
 */
@Data
public class LoginResetPwdMailReqVo {

	/**
	 * 验证码
	 */
	@NotEmpty(message = "验证码不能为空")
	private String code;

	/**
	 * 新密码
	 */
	@NotEmpty(message = "新密码不能为空")
	private String newPassword;

}
