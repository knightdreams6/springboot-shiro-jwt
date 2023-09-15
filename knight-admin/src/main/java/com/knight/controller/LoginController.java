package com.knight.controller;

import com.knight.entity.base.R;
import com.knight.interceptor.annotation.RequestLimit;
import com.knight.shiro.service.LoginService;
import com.knight.valid.annotation.PhoneNumber;
import com.knight.vo.request.LoginCodeReqVo;
import com.knight.vo.request.LoginMailCodeReqVo;
import com.knight.vo.request.LoginPasswordReqVo;
import com.knight.vo.request.LoginPasswordUpdateReqVo;
import com.knight.vo.request.LoginResetPwdMailReqVo;
import com.knight.vo.request.LoginSendResetPwdMailReqVo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;

/**
 * @author lixiao
 * @version 1.0
 * @since 2020/4/15 16:51
 */
@Api(tags = "【user】登录")
@RestController
@RequestMapping("/login")
@RequiredArgsConstructor
@Validated
public class LoginController {

	private final LoginService loginService;

	@ApiOperation("根据邮箱验证码重置密码")
	@PostMapping("/mail/reset-pwd")
	public R<Object> resetPwdByMail(@Validated @RequestBody LoginResetPwdMailReqVo reqVo) {
		return R.bool(loginService.resetPwdByMail(reqVo.getCode(), reqVo.getNewPassword()));
	}

	@ApiOperation("发送重置密码链接邮件")
	@GetMapping("/mail/reset-pwd")
	public R<Object> sendResetPwdLinkMail(@Validated LoginSendResetPwdMailReqVo reqVo) {
		return R.bool(loginService.sendResetPwdLinkMail(reqVo.getUsername(), reqVo.getRedirectUri()));
	}

	@ApiOperation("发送邮箱登陆验证码")
	@GetMapping("/mail/code")
	public R<Object> sendLoginMailCode(@NotEmpty @Email @ApiParam(value = "邮箱") @RequestParam String mail) {
		return R.bool(loginService.sendLoginMailCode(mail));
	}

	@ApiOperation("邮箱验证码登录")
	@PostMapping("/mail/code")
	public R<Object> loginByMailCode(@Validated @RequestBody LoginMailCodeReqVo reqVo) {
		return loginService.loginByMailCode(reqVo.getMail(), reqVo.getCode());
	}

	@RequestLimit(second = 60 * 60, maxCount = 10)
	@ApiOperation(value = "发送登录验证码")
	@GetMapping("/code")
	public R<Object> sendLoginCode(@PhoneNumber @ApiParam(value = "手机号") @RequestParam String phone) {
		return R.bool(loginService.sendLoginCode(phone));
	}

	@RequestLimit(second = 60 * 60 * 24, maxCount = 5)
	@ApiOperation("发送修改密码验证码")
	@GetMapping("/modify-pwd-code")
	public R<Object> sendModifyPasswordCode(@PhoneNumber @ApiParam(value = "手机号") @RequestParam String phone) {
		return R.bool(loginService.sendModifyPasswordCode(phone));
	}

	@ApiOperation("通过手机验证码修改密码")
	@PutMapping("/password")
	public R<Object> modifyPassword(@Validated @RequestBody LoginPasswordUpdateReqVo reqVo) {
		return loginService.modifyPassword(reqVo.getUsername(), reqVo.getCode(), reqVo.getPassword());
	}

	@ApiOperation("密码登录")
	@PostMapping("/password")
	public R<Object> loginByPassword(@Validated @RequestBody LoginPasswordReqVo reqVo) {
		return loginService.loginByPassword(reqVo);
	}

	@ApiOperation("验证码登录")
	@PostMapping("/code")
	public R<Object> loginByCode(@Validated @RequestBody LoginCodeReqVo reqVo) {
		return loginService.loginByCode(reqVo.getUsername(), reqVo.getCode());
	}

	@ApiOperation("token刷新")
	@PostMapping("/token/refresh")
	public R<Object> tokenRefresh(@NotEmpty @RequestParam String refreshToken) {
		return loginService.tokenRefresh(refreshToken);
	}

}
