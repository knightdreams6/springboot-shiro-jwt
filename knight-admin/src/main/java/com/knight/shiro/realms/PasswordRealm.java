package com.knight.shiro.realms;

import cn.hutool.core.util.ObjectUtil;
import com.knight.entity.orm.SysUser;
import com.knight.service.ISysUserService;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.SimpleAuthenticationInfo;
import org.apache.shiro.authc.UnknownAccountException;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.authc.credential.DefaultPasswordService;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.codec.Base64;
import org.apache.shiro.crypto.hash.Sha256Hash;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;
import org.apache.shiro.util.ByteSource;

import javax.annotation.Resource;

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
		log.info(token.getUsername() + " - password auth start...");
		// 根据手机号查询用户
		SysUser user = userService.selectUserByPhone(token.getUsername());
		if (ObjectUtil.isNull(user)) {
			// 抛出账号不存在异常
			throw new UnknownAccountException(UsernamePasswordToken.class.getName());
		}
		// 1.principal：认证的实体信息，可以是手机号，也可以是数据表对应的用户的实体类对象
		// 2.credentials：密码
		Sha256Hash credentials = Sha256Hash.fromHexString(user.getSuPassword());
		credentials.setSalt(ByteSource.Util.bytes(Base64.decode(user.getSuSalt())));
		credentials.setIterations(DefaultPasswordService.DEFAULT_HASH_ITERATIONS);
		// 3.realmName：当前realm对象的name，调用父类的getName()方法即可
		String realmName = super.getName();
		return new SimpleAuthenticationInfo(user, credentials, realmName);
	}

}
