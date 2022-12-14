package com.knight.shiro.realms;

import cn.hutool.extra.spring.SpringUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.BearerToken;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.authc.pam.ModularRealmAuthenticator;
import org.apache.shiro.realm.Realm;

import java.util.Collection;

@Slf4j
public class CustomModularRealmAuthenticator extends ModularRealmAuthenticator {

	/**
	 * 重写多realm认证
	 * @param realms 领域
	 * @param token 令牌
	 * @return {@link AuthenticationInfo}
	 */
	@Override
	protected AuthenticationInfo doMultiRealmAuthentication(Collection<Realm> realms, AuthenticationToken token) {
		Realm finalRealm;
		// 匹配Realm名称
		if (token instanceof BearerToken) {
			finalRealm = SpringUtil.getBean(OauthRealm.class);
		}
		else if (token instanceof UsernamePasswordToken) {
			finalRealm = SpringUtil.getBean(PasswordRealm.class);
		}
		else {
			finalRealm = SpringUtil.getBean(CodeRealm.class);
		}
		return super.doSingleRealmAuthentication(finalRealm, token);
	}

}
