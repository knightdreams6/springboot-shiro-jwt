package com.knight.vo.request;

import com.knight.valid.annotation.PhoneNumber;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotEmpty;

/**
 * 登录密码请求参数vo
 *
 * @author knight
 * @since 2023/01/15
 */
@Data
@ApiModel(value = "密码登陆请求参数")
public class LoginPasswordReqVo {

	@PhoneNumber
	@NotEmpty(message = "用户名不能为空")
	@ApiModelProperty(value = "用户名", required = true)
	private String username;

	@NotEmpty(message = "密码不能为空")
	@ApiModelProperty(value = "密码", required = true)
	private String password;

}
