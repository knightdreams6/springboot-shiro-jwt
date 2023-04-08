package com.knight.controller;

import com.knight.entity.base.R;
import com.knight.interceptor.annotation.RequestLimit;
import com.knight.shiro.service.LoginService;
import com.knight.valid.annotation.PhoneNumber;
import com.knight.vo.request.LoginPasswordReqVo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.RequiredArgsConstructor;
import org.apache.shiro.authz.annotation.RequiresUser;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.constraints.NotEmpty;

/**
 * @author lixiao
 * @version 1.0
 * @date 2020/4/15 16:51
 */
@Api(tags = "【user】登录")
@RestController
@RequestMapping("/login")
@RequiredArgsConstructor
@Validated
public class LoginController {

	private final LoginService loginService;

	@RequestLimit(second = 60 * 60, maxCount = 10)
	@ApiOperation(value = "发送登录验证码")
	@ApiImplicitParam(name = "phone", value = "手机号", required = true, paramType = "query", dataTypeClass = String.class)
	@GetMapping("/code")
	public R<Object> sendLoginCode(@PhoneNumber @ApiParam(value = "手机号") @RequestParam String phone) {
		return R.bool(loginService.sendLoginCode(phone));
	}

	@RequiresUser
	@RequestLimit(second = 60 * 60 * 24, maxCount = 5)
	@ApiOperation("发送修改密码验证码")
	@ApiImplicitParam(name = "phone", value = "手机号", required = true, paramType = "query", dataTypeClass = String.class)
	@GetMapping("/modify-pwd-code")
	public R<Object> sendModifyPasswordCode(@PhoneNumber String phone) {
		return R.bool(loginService.sendModifyPasswordCode(phone));
	}

	@RequiresUser
	@ApiOperation("通过手机验证码修改密码")
	@ApiImplicitParams({
			@ApiImplicitParam(name = "phone", value = "手机号", required = true, paramType = "query",
					dataTypeClass = String.class),
			@ApiImplicitParam(name = "code", value = "验证码", required = true, paramType = "query",
					dataTypeClass = String.class),
			@ApiImplicitParam(name = "password", value = "密码", required = true, paramType = "query",
					dataTypeClass = String.class) })
	@PutMapping("/password")
	public R<Object> modifyPassword(@PhoneNumber String phone, @NotEmpty(message = "验证码不能为空") String code,
			@NotEmpty(message = "密码不能为空") String password) {
		return loginService.modifyPassword(phone, code, password);
	}

	@ApiOperation("密码登录")
	@PostMapping("/password")
	public R<Object> loginByPassword(@RequestBody LoginPasswordReqVo reqVo) {
		return loginService.loginByPassword(reqVo);
	}

	@ApiOperation("验证码登录")
	@ApiImplicitParams({
			@ApiImplicitParam(name = "phone", value = "手机号", required = true, paramType = "query",
					dataTypeClass = String.class),
			@ApiImplicitParam(name = "code", value = "验证码", required = true, paramType = "query",
					dataTypeClass = String.class), })
	@PostMapping("/code")
	public R<Object> loginByCode(@PhoneNumber String phone, @NotEmpty(message = "验证码不能为空") String code) {
		return loginService.loginByCode(phone, code);
	}

	@ApiOperation("token刷新")
	@PostMapping("/token/refresh")
	public R<Object> tokenRefresh(@RequestParam String refreshToken) {
		return loginService.tokenRefresh(refreshToken);
	}

}
