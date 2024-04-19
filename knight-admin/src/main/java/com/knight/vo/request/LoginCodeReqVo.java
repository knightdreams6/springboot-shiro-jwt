package com.knight.vo.request;

import com.knight.valid.annotation.PhoneNumber;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import lombok.Data;

/**
 * 验证码登录请求参数vo
 *
 * @author knight
 * @since 2023/07/09
 */
@Data
public class LoginCodeReqVo {

	/**
	 * 用户名
	 */
	@PhoneNumber
	private String username;

	/**
	 * 验证码
	 */
	@Size(min = 4, max = 4)
	@NotEmpty(message = "验证码不能为空")
	private String code;

}
