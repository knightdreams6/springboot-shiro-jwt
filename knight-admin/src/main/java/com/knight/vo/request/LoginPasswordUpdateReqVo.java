package com.knight.vo.request;

import com.knight.valid.annotation.PhoneNumber;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotEmpty;

/**
 * 登录密码更新请求参数vo
 *
 * @author knight
 * @since 2023/07/12
 */
@Data
@ApiModel(value = "登录密码更新请求参数vo")
public class LoginPasswordUpdateReqVo {

	@PhoneNumber
	@NotEmpty(message = "用户名不能为空")
	@ApiModelProperty(value = "用户名", required = true)
	private String username;

	@NotEmpty(message = "验证码不能为空")
	@ApiModelProperty(value = "密码", required = true)
	private String code;

	@NotEmpty(message = "密码不能为空")
	@ApiModelProperty(value = "密码", required = true)
	private String password;

}
