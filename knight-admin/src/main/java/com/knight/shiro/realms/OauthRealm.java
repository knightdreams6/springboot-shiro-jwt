package com.knight.shiro.realms;

import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import com.knight.entity.base.LoginUser;
import com.knight.entity.orm.SysUser;
import com.knight.service.ISysUserService;
import com.knight.shiro.service.TokenService;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.authc.*;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;

import javax.annotation.Resource;

/**
 * @author lixiao
 * @date 2019/8/6 10:02
 * @see org.apache.shiro.web.filter.authc.BearerHttpAuthenticationFilter
 */
@Slf4j
public class OauthRealm extends AuthorizingRealm {

	/**
	 * 令牌服务
	 */
	@Resource
	private TokenService tokenService;

	/**
	 * 用户服务
	 */
	@Resource
	private ISysUserService userService;

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
		// 获得phone
		String phone = tokenService.getPhone(token);
		log.info(phone + " - token auth start...");
		// 如果获取到的手机号为空
		if (StrUtil.isBlank(phone)) {
			throw new IncorrectCredentialsException();
		}
		SysUser user = userService.selectUserByPhone(phone);
		if (ObjectUtil.isNull(user)) {
			throw new IncorrectCredentialsException();
		}
		boolean verify = tokenService.verify(token, user.getSuPassword());
		if (!verify) {
			throw new IncorrectCredentialsException();
		}
		return new SimpleAuthenticationInfo(token, token, getName());
	}

}
