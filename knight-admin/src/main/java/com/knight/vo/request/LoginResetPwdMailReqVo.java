package com.knight.vo.request;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotEmpty;

/**
 * 邮箱重置密码请求参数
 *
 * @author knight
 */
@Data
@ApiModel(value = "邮箱验证码登录请求参数vo")
public class LoginResetPwdMailReqVo {

	@NotEmpty(message = "验证码不能为空")
	@ApiModelProperty(value = "验证码", required = true)
	private String code;

	@NotEmpty(message = "新密码不能为空")
	@ApiModelProperty(value = "新密码", required = true)
	private String newPassword;

}
