package com.knight.vo.request;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 登录密码请求参数vo
 *
 * @author knight
 * @since 2023/01/15
 */
@Data
@ApiModel(value = "密码登陆请求参数")
public class LoginPasswordReqVo {

	@ApiModelProperty(value = "用户名")
	private String username;

	@ApiModelProperty(value = "密码")
	private String password;

}
