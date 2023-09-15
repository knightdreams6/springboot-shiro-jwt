package com.knight.vo.request;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotEmpty;

/**
 * 重置密码邮件请求参数
 *
 * @author knight
 */
@Data
@ApiModel(value = "邮箱验证码登录请求参数vo")
public class LoginSendResetPwdMailReqVo {

	@NotEmpty(message = "用户名不能为空")
	@ApiModelProperty(value = "用户名", required = true)
	private String username;

	@NotEmpty(message = "重定向地址不能为空")
	@ApiModelProperty(value = "重定向地址", required = true)
	private String redirectUri;

}
