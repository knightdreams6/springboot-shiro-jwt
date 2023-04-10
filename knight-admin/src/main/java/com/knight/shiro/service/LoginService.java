package com.knight.shiro.service;

import cn.hutool.core.util.IdUtil;
import com.knight.entity.base.R;
import com.knight.entity.constans.Constant;
import com.knight.entity.constans.RedisKey;
import com.knight.entity.enums.CommonResultConstants;
import com.knight.entity.orm.SysUser;
import com.knight.service.ISysUserService;
import com.knight.shiro.token.PhoneCodeToken;
import com.knight.utils.CommonsUtils;
import com.knight.vo.request.LoginPasswordReqVo;
import lombok.RequiredArgsConstructor;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.ExpiredCredentialsException;
import org.apache.shiro.authc.IncorrectCredentialsException;
import org.apache.shiro.authc.UnknownAccountException;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.subject.Subject;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.TimeUnit;

/**
 * @author lixiao
 * @date 2020/4/15 16:50
 */
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
	 * 发送登录验证码
	 * @param phone 电话
	 * @return boolean
	 */
	public boolean sendLoginCode(String phone) {
		// 这里使用默认值
		int code = 6666;
		// todo 此处为发送验证码代码

		// 将验证码加密后存储到redis中
		String encryptCode = CommonsUtils.encryptPassword(String.valueOf(code), phone);
		stringRedisTemplate.opsForValue()
			.set(RedisKey.getLoginCodeKey(phone), encryptCode, Constant.CODE_EXPIRE_TIME, TimeUnit.MINUTES);
		return true;
	}

	public boolean sendModifyPasswordCode(String phone) {
		int code = 6666;
		// todo 此处为发送验证码代码
		// 将验证码加密后存储到redis中
		String encryptCode = CommonsUtils.encryptPassword(String.valueOf(code), phone);
		stringRedisTemplate.opsForValue()
			.set(RedisKey.getModifyPasswordCodeKey(phone), encryptCode, Constant.CODE_EXPIRE_TIME, TimeUnit.MINUTES);
		return true;
	}

	public R<Object> loginByPassword(LoginPasswordReqVo reqVo) {
		// 1.获取Subject
		Subject subject = SecurityUtils.getSubject();
		// 2.封装用户数据
		UsernamePasswordToken token = new UsernamePasswordToken(reqVo.getUsername(), reqVo.getPassword());
		// 3.执行登录方法
		try {
			subject.login(token);
			return R.ok(returnLoginInitParam(reqVo.getUsername()));
		}
		catch (UnknownAccountException e) {
			return R.failed(CommonResultConstants.USERNAME_NOT_EXIST);
		}
		catch (IncorrectCredentialsException e) {
			return R.failed(CommonResultConstants.PASSWORD_ERROR);
		}
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
		try {
			subject.login(token);
			return R.ok(returnLoginInitParam(phone));
		}
		catch (UnknownAccountException e) {
			return R.failed(CommonResultConstants.USERNAME_NOT_EXIST);
		}
		catch (ExpiredCredentialsException e) {
			return R.failed(CommonResultConstants.CODE_EXPIRE);
		}
		catch (IncorrectCredentialsException e) {
			return R.failed(CommonResultConstants.CODE_ERROR);
		}
	}

	/**
	 * 修改密码
	 * @param phone 手机号
	 * @param code 验证码
	 * @param password 密码
	 * @return {@link R}
	 */
	public R<Object> modifyPassword(String phone, String code, String password) {
		Object modifyCode = stringRedisTemplate.opsForValue().get(RedisKey.getModifyPasswordCodeKey(phone));
		// 判断redis中是否存在验证码
		if (Objects.isNull(modifyCode)) {
			return R.failed(CommonResultConstants.CODE_EXPIRE);
		}
		// 判断redis中code与传递过来的code 是否相等
		if (!Objects.equals(code, modifyCode.toString())) {
			return R.failed(CommonResultConstants.CODE_ERROR);
		}
		SysUser user = userService.selectUserByPhone(phone);
		// 如果用户不存在，执行注册
		if (Objects.isNull(user)) {
			Boolean flag = userService.register(phone, password);
			if (flag) {
				return R.ok(this.returnLoginInitParam(phone));
			}
			else {
				return R.failed();
			}
		}
		// 加密所需盐值
		String salt = IdUtil.simpleUUID();
		// 加密后的密码
		String encryptPassword = CommonsUtils.encryptPassword(password, salt);
		user.setSuSalt(salt);
		user.setSuPassword(encryptPassword);
		// 删除缓存
		stringRedisTemplate.delete(RedisKey.getLoginUserKey(phone));
		boolean flag = userService.updateById(user);
		if (flag) {
			return R.ok(this.returnLoginInitParam(phone));
		}
		return R.failed();
	}

	/**
	 * 返回登录后初始化参数
	 * @param phone phone
	 * @return Map<String, Object>
	 */
	private Map<String, Object> returnLoginInitParam(String phone) {
		Map<String, Object> data = new HashMap<>(4);
		// 根据手机号查询用户
		SysUser user = userService.selectUserByPhone(phone);
		// 生成jwtToken
		String token = tokenService.createToken(phone, user.getId(), user.getSuPassword(), Constant.TOKEN_EXPIRE_TIME);
		// 生成刷新token
		String refreshToken = tokenService.createToken(phone, user.getId(), user.getSuPassword(),
				Constant.TOKEN_REFRESH_TIME);
		// token
		data.put("token", token);
		// 刷新时所需token
		data.put("refreshToken", refreshToken);
		return data;
	}

	/**
	 * token刷新
	 * @return Result
	 */
	public R<Object> tokenRefresh(String refreshToken) {
		String phone = tokenService.getPhone(refreshToken);
		SysUser user = userService.selectUserByPhone(phone);
		boolean verify = tokenService.verify(refreshToken, user.getSuPassword());
		if (!verify) {
			return R.failed(CommonResultConstants.REFRESH_TOKEN_INVALID);
		}
		Map<String, Object> data = new HashMap<>(4);
		// 生成jwtToken
		String newToken = tokenService.createToken(phone, user.getId(), user.getSuPassword(),
				Constant.TOKEN_EXPIRE_TIME);
		// 生成刷新token
		String newRefreshToken = tokenService.createToken(phone, user.getId(), user.getSuPassword(),
				Constant.TOKEN_REFRESH_TIME);
		// toke
		data.put("token", newToken);
		// 刷新时所需token
		data.put("refreshToken", newRefreshToken);
		return R.ok(data);
	}

}
