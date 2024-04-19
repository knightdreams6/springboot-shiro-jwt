package com.knight.shiro.realms;

import cn.hutool.core.util.ObjectUtil;
import com.knight.entity.orm.SysUser;
import com.knight.service.ISysUserService;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.authc.*;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;

/**
 * @author lixiao
 * @since 2019/7/31 11:40
 */
@Slf4j
public class PasswordRealm extends AuthorizingRealm {

	@Resource
	private ISysUserService userService;

	@Override
	public boolean supports(AuthenticationToken token) {
		return token instanceof UsernamePasswordToken;
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
		UsernamePasswordToken token = (UsernamePasswordToken) authenticationToken;
		log.info("{} - password auth start...", token.getUsername());
		// 根据手机号查询用户
		SysUser user = userService.selectUserByPhone(token.getUsername());
		if (ObjectUtil.isNull(user)) {
			// 抛出账号不存在异常
			throw new UnknownAccountException(UsernamePasswordToken.class.getName());
		}
		return new SimpleAuthenticationInfo(user, user.getSuPassword(), null, super.getName());
	}

}
