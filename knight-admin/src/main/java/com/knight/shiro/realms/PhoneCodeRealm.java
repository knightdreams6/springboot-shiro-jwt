package com.knight.shiro.realms;

import cn.hutool.core.util.ObjectUtil;
import com.knight.entity.constans.RedisKey;
import com.knight.entity.orm.SysUser;
import com.knight.service.ISysUserService;
import com.knight.shiro.token.PhoneCodeToken;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.ExpiredCredentialsException;
import org.apache.shiro.authc.SimpleAuthenticationInfo;
import org.apache.shiro.authc.UnknownAccountException;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.lang.util.ByteSource;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;
import org.springframework.data.redis.core.StringRedisTemplate;

import javax.annotation.Resource;

/**
 * @author lixiao
 * @since 2019/7/31 11:40
 */
@Slf4j
public class PhoneCodeRealm extends AuthorizingRealm {

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
		return token instanceof PhoneCodeToken;
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
		PhoneCodeToken token = (PhoneCodeToken) authenticationToken;
		log.info(token.getPhone() + " - phone code auth start...");
		// 根据手机号查询用户
		SysUser user = userService.selectUserByPhone(token.getPhone());
		if (ObjectUtil.isNull(user)) {
			// 抛出账号不存在异常
			throw new UnknownAccountException(PhoneCodeToken.class.getName());
		}
		// 1.principal：认证的实体信息，可以是手机号，也可以是数据表对应的用户的实体类对象
		// 2.从redis中获取登录验证码
		String loginCodeKey = RedisKey.getLoginCodeKey(user.getSuPhone());
		Object credentials = stringRedisTemplate.opsForValue().get(loginCodeKey);
		if (credentials == null) {
			throw new ExpiredCredentialsException();
		}
		// 移除验证码
		stringRedisTemplate.delete(loginCodeKey);
		// 3.realmName：当前realm对象的name，调用父类的getName()方法即可
		String realmName = super.getName();
		// 4.盐,取用户信息中唯一的字段来生成盐值，避免由于两个用户原始密码相同，加密后的密码也相同
		ByteSource credentialsSalt = ByteSource.Util.bytes(token.getPhone());
		return new SimpleAuthenticationInfo(user, credentials, credentialsSalt, realmName);
	}

	@Override
	protected void assertCredentialsMatch(AuthenticationToken token, AuthenticationInfo info)
			throws AuthenticationException {
		super.assertCredentialsMatch(token, info);
	}

}
