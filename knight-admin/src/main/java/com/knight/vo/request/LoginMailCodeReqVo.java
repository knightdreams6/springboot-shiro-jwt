package com.knight.vo.request;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;

/**
 * 邮箱验证码登录请求参数vo
 *
 * @author knight
 */
@Data
@ApiModel(value = "邮箱验证码登录请求参数vo")
public class LoginMailCodeReqVo {

	@Email
	@NotEmpty(message = "邮箱不能为空")
	@ApiModelProperty(value = "邮箱", required = true)
	private String mail;

	@Size(min = 6, max = 6)
	@NotEmpty(message = "验证码不能为空")
	@ApiModelProperty(value = "验证码", required = true)
	private String code;

}
