package com.knight.shiro.realms;

import cn.hutool.core.util.ObjectUtil;
import com.knight.entity.constans.RedisKey;
import com.knight.entity.orm.SysUser;
import com.knight.service.ISysUserService;
import com.knight.shiro.token.MailCodeToken;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.ExpiredCredentialsException;
import org.apache.shiro.authc.SimpleAuthenticationInfo;
import org.apache.shiro.authc.UnknownAccountException;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;
import org.apache.shiro.util.ByteSource;
import org.springframework.data.redis.core.StringRedisTemplate;

import javax.annotation.Resource;

/**
 * 邮箱验证码
 *
 * @author lixiao
 */
@Slf4j
public class MailCodeRealm extends AuthorizingRealm {

	/**
	 * 用户服务
	 */
	@Resource
	private ISysUserService userService;

	/**
	 * redis模板
	 */
	@Resource
	private StringRedisTemplate stringRedisTemplate;

	@Override
	public boolean supports(AuthenticationToken token) {
		return token instanceof MailCodeToken;
	}

	/**
	 * 获取授权信息
	 * @param principals principals
	 * @return AuthorizationInfo
	 */
	@Override
	protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principals) {
		return null;
	}

	/**
	 * 获取身份认证信息
	 * @param authenticationToken 身份认证凭据
	 * @return AuthenticationInfo 身份认证信息
	 * @throws AuthenticationException 身份认证期间抛出的异常
	 */
	@Override
	protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken authenticationToken)
			throws AuthenticationException {
		MailCodeToken token = (MailCodeToken) authenticationToken;
		log.info(token.getMail() + " - mail code auth start...");
		// 根据邮箱查询用户
		SysUser user = userService.selectUserByMail(token.getMail());
		if (ObjectUtil.isNull(user)) {
			// 抛出账号不存在异常
			throw new UnknownAccountException(MailCodeToken.class.getName());
		}
		// 1.principal：认证的实体信息，可以是手机号，也可以是数据表对应的用户的实体类对象
		// 2.从redis中获取登录验证码
		String loginCodeKey = RedisKey.getLoginCodeKey(user.getSuMail());
		Object credentials = stringRedisTemplate.opsForValue().get(loginCodeKey);
		if (credentials == null) {
			throw new ExpiredCredentialsException();
		}
		// 移除验证码
		stringRedisTemplate.delete(loginCodeKey);
		// 3.realmName：当前realm对象的name，调用父类的getName()方法即可
		String realmName = super.getName();
		// 4.盐,取用户信息中唯一的字段来生成盐值，避免由于两个用户原始密码相同，加密后的密码也相同
		ByteSource credentialsSalt = ByteSource.Util.bytes(token.getMail());
		return new SimpleAuthenticationInfo(user, credentials, credentialsSalt, realmName);
	}

}
