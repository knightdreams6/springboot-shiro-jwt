package com.knight.shiro.service;

import cn.hutool.core.io.IoUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.RandomUtil;
import cn.hutool.core.util.StrUtil;
import com.knight.entity.base.R;
import com.knight.entity.constans.Constant;
import com.knight.entity.constans.RedisKey;
import com.knight.entity.enums.CommonResultConstants;
import com.knight.entity.orm.SysUser;
import com.knight.exception.ServiceException;
import com.knight.message.enums.MessageContentTypeEnums;
import com.knight.message.mail.MailMessage;
import com.knight.message.support.MessageBuilder;
import com.knight.message.support.MessageTemplate;
import com.knight.service.ISysUserService;
import com.knight.shiro.token.MailCodeToken;
import com.knight.shiro.token.PhoneCodeToken;
import com.knight.utils.CommonsUtils;
import com.knight.vo.request.LoginPasswordReqVo;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.authc.credential.HashingPasswordService;
import org.apache.shiro.crypto.hash.Hash;
import org.apache.shiro.subject.Subject;
import org.springframework.core.io.ClassPathResource;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.nio.charset.Charset;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.TimeUnit;

/**
 * @author lixiao
 * @since 2020/4/15 16:50
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class LoginService {

	/**
	 * 用户服务
	 */
	private final ISysUserService userService;

	/**
	 * redis模板
	 */
	private final StringRedisTemplate stringRedisTemplate;

	/**
	 * 令牌服务
	 */
	private final TokenService tokenService;

	/**
	 * 消息模板
	 */
	private final MessageTemplate messageTemplate;

	/**
	 * 密码服务
	 */
	private final HashingPasswordService passwordService;

	/**
	 * mailCode登录
	 * @param mail mail
	 * @param code mailCode
	 * @return {@link R}<{@link Object}>
	 */
	public R<Object> loginByMailCode(String mail, String code) {
		// 1.获取Subject
		Subject subject = SecurityUtils.getSubject();
		// 2.封装用户数据
		MailCodeToken token = new MailCodeToken(mail, code);
		// 3.执行登录方法
		subject.login(token);
		return R.ok(returnLoginInitParam(mail));
	}

	/**
	 * 根据邮箱验证码重置密码
	 */
	public boolean resetPwdByMail(String code, String newPassword) {
		String resetPwdVerifyCodeKey = RedisKey.getResetPwdVerifyCodeKey(code);
		String username = stringRedisTemplate.opsForValue().get(resetPwdVerifyCodeKey);
		if (StrUtil.isBlank(username)) {
			throw new ServiceException(CommonResultConstants.MAIL_CODE_EXPIRE);
		}
		SysUser user = userService.selectUserByPhone(username);
		if (ObjectUtil.isNull(user)) {
			throw new ServiceException(CommonResultConstants.MAIL_CODE_EXPIRE);
		}

		// 加密后的密码
		Hash encryptPassword = passwordService.hashPassword(newPassword);
		user.setSuSalt(encryptPassword.getSalt().toBase64());
		user.setSuPassword(encryptPassword.toHex());
		stringRedisTemplate.delete(resetPwdVerifyCodeKey);
		return userService.updateById(user);
	}

	/**
	 * 发送重置密码链接验
	 */
	public boolean sendResetPwdLinkMail(String username, String redirectUri) {
		SysUser sysUser = userService.selectUserByPhone(username);
		if (ObjectUtil.isNull(sysUser)) {
			throw new ServiceException(CommonResultConstants.USERNAME_OR_EMAIL_NOT_FOUND);
		}
		ClassPathResource classPathResource = new ClassPathResource("/mail/resetPwdEmail.html");
		String content;
		try {
			content = IoUtil.read(classPathResource.getInputStream(), Charset.defaultCharset());
		}
		catch (IOException e) {
			log.error("邮件发送失败: {}", e.getLocalizedMessage());
			return false;
		}

		// 验证码
		String verifyCode = RandomUtil.randomString(8);

		content = StrUtil.replace(content, "${username}", sysUser.getSuName());
		content = StrUtil.replace(content, "${resetLink}", redirectUri + "?code=" + verifyCode);
		MailMessage mailMessage = MailMessage.builder()
			.receiver(Collections.singleton(sysUser.getSuMail()))
			.subject("Retrieve password")
			.content(content)
			.contentType(MessageContentTypeEnums.HTML)
			.build();
		messageTemplate.sendAsync(MessageBuilder.withPayload(mailMessage).build());
		stringRedisTemplate.opsForValue()
			.set(RedisKey.getResetPwdVerifyCodeKey(verifyCode), username, Constant.CODE_EXPIRE_TIME, TimeUnit.MINUTES);
		return true;
	}

	/**
	 * 发送登录验证码
	 * @param mail 邮箱
	 * @return boolean
	 */
	public boolean sendLoginMailCode(String mail) {
		// 验证码
		String verifyCode = String.valueOf(CommonsUtils.getCode());

		ClassPathResource classPathResource = new ClassPathResource("/mail/loginCodeEmail.html");
		String content;
		try {
			content = IoUtil.read(classPathResource.getInputStream(), Charset.defaultCharset());
		}
		catch (IOException e) {
			log.error("邮件发送失败: {}", e.getLocalizedMessage());
			return false;
		}

		content = StrUtil.replace(content, "${verifyCode}", verifyCode);
		MailMessage mailMessage = MailMessage.builder()
			.receiver(Collections.singleton(mail))
			.subject("Login Code")
			.content(content)
			.contentType(MessageContentTypeEnums.HTML)
			.build();
		messageTemplate.sendAsync(MessageBuilder.withPayload(mailMessage).build());

		// 将验证码加密后存储到redis中
		String encryptCode = CommonsUtils.encryptCode(verifyCode, mail);
		stringRedisTemplate.opsForValue()
			.set(RedisKey.getLoginCodeKey(mail), encryptCode, Constant.CODE_EXPIRE_TIME, TimeUnit.MINUTES);
		return true;
	}

	/**
	 * 发送登录验证码
	 * @param phone 电话
	 * @return boolean
	 */
	public boolean sendLoginCode(String phone) {
		// 这里使用默认值
		int code = 6666;
		// todo 此处为发送验证码代码

		// 将验证码加密后存储到redis中
		String encryptCode = CommonsUtils.encryptCode(String.valueOf(code), phone);
		stringRedisTemplate.opsForValue()
			.set(RedisKey.getLoginCodeKey(phone), encryptCode, Constant.CODE_EXPIRE_TIME, TimeUnit.MINUTES);
		return true;
	}

	public boolean sendModifyPasswordCode(String phone) {
		int code = 6666;
		// todo 此处为发送验证码代码
		// 将验证码加密后存储到redis中
		stringRedisTemplate.opsForValue()
			.set(RedisKey.getModifyPasswordCodeKey(phone), String.valueOf(code), Constant.CODE_EXPIRE_TIME,
					TimeUnit.MINUTES);
		return true;
	}

	public R<Object> loginByPassword(LoginPasswordReqVo reqVo) {
		// 1.获取Subject
		Subject subject = SecurityUtils.getSubject();
		// 2.封装用户数据
		UsernamePasswordToken token = new UsernamePasswordToken(reqVo.getUsername(), reqVo.getPassword());
		// 3.执行登录方法
		subject.login(token);
		return R.ok(returnLoginInitParam(reqVo.getUsername()));
	}

	/**
	 * 手机号验证码登录
	 * @param phone 电话
	 * @param code 代码
	 * @return {@link R}
	 */
	public R<Object> loginByCode(String phone, String code) {
		// 1.获取Subject
		Subject subject = SecurityUtils.getSubject();
		SysUser sysUser = userService.selectUserByPhone(phone);
		// 2.验证码登录，如果该用户不存在则创建该用户
		if (Objects.isNull(sysUser)) {
			// 2.1 注册
			userService.register(phone);
		}
		// 3.封装用户数据
		PhoneCodeToken token = new PhoneCodeToken(phone, code);
		// 4.执行登录方法
		subject.login(token);
		return R.ok(returnLoginInitParam(phone));
	}

	/**
	 * 修改密码
	 * @param username 用户名
	 * @param code 验证码
	 * @param password 密码
	 * @return {@link R}
	 */
	public R<Object> modifyPassword(String username, String code, String password) {
		String modifyCode = stringRedisTemplate.opsForValue().get(RedisKey.getModifyPasswordCodeKey(username));
		// 判断redis中是否存在验证码
		if (Objects.isNull(modifyCode)) {
			return R.failed(CommonResultConstants.CODE_EXPIRE);
		}
		// 判断redis中code与传递过来的code 是否相等
		if (!Objects.equals(code, modifyCode)) {
			return R.failed(CommonResultConstants.CODE_ERROR);
		}
		SysUser user = userService.selectUserByPhone(username);
		// 如果用户不存在，执行注册
		if (Objects.isNull(user)) {
			Boolean flag = userService.register(username, password);
			if (flag) {
				return R.ok(this.returnLoginInitParam(username));
			}
			else {
				return R.failed();
			}
		}

		// 加密后的密码
		Hash encryptPassword = passwordService.hashPassword(password);
		user.setSuSalt(encryptPassword.getSalt().toBase64());
		user.setSuPassword(encryptPassword.toHex());
		// 删除缓存
		stringRedisTemplate.delete(RedisKey.getModifyPasswordCodeKey(username));
		boolean flag = userService.updateById(user);
		if (flag) {
			return R.ok(this.returnLoginInitParam(username));
		}
		return R.failed();
	}

	/**
	 * 返回登录后初始化参数
	 * @param username 用户名
	 * @return Map<String, Object>
	 */
	private Map<String, Object> returnLoginInitParam(String username) {
		Map<String, Object> data = new HashMap<>(4);
		// 生成访问Token
		String token = tokenService.createAccessToken(username);
		// 生成刷新token
		String refreshToken = tokenService.createRefreshToken(username);
		// token
		data.put("token", token);
		// 刷新时所需token
		data.put("refreshToken", refreshToken);
		return data;
	}

	/**
	 * 令牌刷新
	 * @param refreshToken 刷新令牌
	 * @return {@link R}<{@link Object}>
	 */
	public R<Object> tokenRefresh(String refreshToken) {
		boolean verify = tokenService.verify(refreshToken);
		if (!verify) {
			return R.failed(CommonResultConstants.REFRESH_TOKEN_INVALID);
		}
		String subject = tokenService.getSubject(refreshToken);
		Map<String, Object> data = new HashMap<>(4);
		// 生成访问Token
		String token = tokenService.createAccessToken(subject);
		// 生成刷新token
		String newRefreshToken = tokenService.createRefreshToken(subject);
		// toke
		data.put("token", token);
		// 刷新时所需token
		data.put("refreshToken", newRefreshToken);
		return R.ok(data);
	}

}
