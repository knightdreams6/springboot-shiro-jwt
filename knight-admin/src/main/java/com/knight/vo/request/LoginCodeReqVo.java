package com.knight.vo.request;

import com.knight.valid.annotation.PhoneNumber;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;

/**
 * 验证码登录请求参数vo
 *
 * @author knight
 * @since 2023/07/09
 */
@Data
@ApiModel(value = "验证码登陆请求参数")
public class LoginCodeReqVo {

	@PhoneNumber
	@ApiModelProperty(value = "用户名", required = true)
	private String username;

	@Size(min = 4, max = 4)
	@NotEmpty(message = "验证码不能为空")
	@ApiModelProperty(value = "验证码", required = true)
	private String code;

}
