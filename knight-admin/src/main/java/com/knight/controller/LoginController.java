package com.knight.controller;

import com.knight.api.limit.annotation.ApiLimit;
import com.knight.entity.base.R;
import com.knight.shiro.service.LoginService;
import com.knight.valid.annotation.PhoneNumber;
import com.knight.vo.request.*;
import com.knight.vo.response.LoginAuthDataVo;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

/**
 * 登录
 *
 * @author knight
 */
@RestController
@RequestMapping("/login")
@RequiredArgsConstructor
@Validated
public class LoginController {

	private final LoginService loginService;

	/**
	 * 根据邮箱验证码重置密码
	 * @param reqVo LoginResetPwdMailReqVo
	 * @return R<Void>
	 */
	@PostMapping("/mail/reset-pwd")
	public R<Void> resetPwdByMail(@Validated @RequestBody LoginResetPwdMailReqVo reqVo) {
		return R.bool(loginService.resetPwdByMail(reqVo.getCode(), reqVo.getNewPassword()));
	}

	/**
	 * 发送重置密码链接邮件
	 * @param reqVo LoginSendResetPwdMailReqVo
	 * @return R<Void>
	 */
	@GetMapping("/mail/reset-pwd")
	public R<Void> sendResetPwdLinkMail(@Validated LoginSendResetPwdMailReqVo reqVo) {
		return R.bool(loginService.sendResetPwdLinkMail(reqVo.getUsername(), reqVo.getRedirectUri()));
	}

	/**
	 * 发送邮箱登陆验证码
	 * @param mail 邮箱地址
	 * @return R<Void>
	 */
	@GetMapping("/mail/code")
	public R<Void> sendLoginMailCode(@NotEmpty @Email @RequestParam String mail) {
		return R.bool(loginService.sendLoginMailCode(mail));
	}

	/**
	 * 邮箱验证码登录
	 * @param reqVo LoginMailCodeReqVo
	 * @return R<LoginAuthDataVo>
	 */
	@PostMapping("/mail/code")
	public R<LoginAuthDataVo> loginByMailCode(@Validated @RequestBody LoginMailCodeReqVo reqVo) {
		return loginService.loginByMailCode(reqVo.getMail(), reqVo.getCode());
	}

	/**
	 * 发送登录验证码
	 * @param phone 手机号
	 * @return R<Void>
	 */
	@ApiLimit(second = 60 * 60, maxCount = 10)
	@GetMapping("/code")
	public R<Void> sendLoginCode(@PhoneNumber @RequestParam String phone) {
		return R.bool(loginService.sendLoginCode(phone));
	}

	/**
	 * 发送修改密码验证码
	 * @param phone 手机号
	 * @return R<Void>
	 */
	@ApiLimit(second = 60 * 60 * 24, maxCount = 5)
	@GetMapping("/modify-pwd-code")
	public R<Void> sendModifyPasswordCode(@PhoneNumber @RequestParam String phone) {
		return R.bool(loginService.sendModifyPasswordCode(phone));
	}

	/**
	 * 通过手机验证码修改密码
	 * @param reqVo LoginPasswordUpdateReqVo
	 * @return R<LoginAuthDataVo>
	 */
	@PutMapping("/password")
	public R<LoginAuthDataVo> modifyPassword(@Validated @RequestBody LoginPasswordUpdateReqVo reqVo) {
		return loginService.modifyPassword(reqVo.getUsername(), reqVo.getCode(), reqVo.getPassword());
	}

	/**
	 * 密码登录
	 * @param reqVo LoginPasswordReqVo
	 * @return R<LoginAuthDataVo>
	 */
	@PostMapping("/password")
	public R<LoginAuthDataVo> loginByPassword(@Validated @RequestBody LoginPasswordReqVo reqVo) {
		return loginService.loginByPassword(reqVo);
	}

	/**
	 * 验证码登录
	 * @param reqVo LoginCodeReqVo
	 * @return R<LoginAuthDataVo>
	 */
	@PostMapping("/code")
	public R<LoginAuthDataVo> loginByCode(@Validated @RequestBody LoginCodeReqVo reqVo) {
		return loginService.loginByCode(reqVo.getUsername(), reqVo.getCode());
	}

	/**
	 * 令牌刷新
	 * @param refreshToken 刷新令牌
	 * @return R<LoginAuthDataVo>
	 */
	@PostMapping("/token/refresh")
	public R<LoginAuthDataVo> tokenRefresh(@NotEmpty @RequestParam String refreshToken) {
		return loginService.tokenRefresh(refreshToken);
	}

}
