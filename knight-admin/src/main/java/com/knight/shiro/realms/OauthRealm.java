package com.knight.shiro.realms;

import com.knight.entity.base.LoginUser;
import com.knight.shiro.service.TokenService;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.authc.*;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;

/**
 * @author lixiao
 * @see org.apache.shiro.web.filter.authc.BearerHttpAuthenticationFilter
 * @since 2019/8/6 10:02
 */
@Slf4j
public class OauthRealm extends AuthorizingRealm {

	/**
	 * 令牌服务
	 */
	@Resource
	private TokenService tokenService;

	@Override
	public boolean supports(AuthenticationToken token) {
		return token instanceof BearerToken;
	}

	/**
	 * 只有当需要检测用户权限的时候才会调用此方法，例如checkRole,checkPermission之类的
	 */
	@Override
	protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principals) {
		LoginUser loginUser = tokenService.getLoginUser();
		SimpleAuthorizationInfo authorizationInfo = new SimpleAuthorizationInfo();
		// 添加角色
		authorizationInfo.addRoles(loginUser.getRoleSet());
		// 添加权限
		authorizationInfo.addStringPermissions(loginUser.getPermissionsSet());
		return authorizationInfo;
	}

	@Override
	protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken authenticationToken)
			throws AuthenticationException {
		BearerToken bearerToken = (BearerToken) authenticationToken;
		// 获取jwtToken
		String token = bearerToken.getToken();
		// 获得username
		String username = tokenService.getSubject(token);
		log.info(username + " - token auth start...");
		boolean verify = tokenService.verify(token);
		if (!verify) {
			throw new IncorrectCredentialsException();
		}
		return new SimpleAuthenticationInfo(username, token, getName());
	}

}
