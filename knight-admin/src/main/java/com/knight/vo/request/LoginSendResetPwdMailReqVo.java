package com.knight.vo.request;

import jakarta.validation.constraints.NotEmpty;
import lombok.Data;

/**
 * 重置密码邮件请求参数
 *
 * @author knight
 */
@Data
public class LoginSendResetPwdMailReqVo {

	/**
	 * 用户名
	 */
	@NotEmpty(message = "用户名不能为空")
	private String username;

	/**
	 * 重定向地址
	 */
	@NotEmpty(message = "重定向地址不能为空")
	private String redirectUri;

}
