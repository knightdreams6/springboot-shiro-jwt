package com.knight.shiro.config;

import com.knight.shiro.filter.OauthFilter;
import com.knight.shiro.realms.CodeRealm;
import com.knight.shiro.realms.CustomModularRealmAuthenticator;
import com.knight.shiro.realms.OauthRealm;
import com.knight.shiro.realms.PasswordRealm;
import org.apache.shiro.authc.Authenticator;
import org.apache.shiro.authc.credential.HashedCredentialsMatcher;
import org.apache.shiro.authc.pam.AtLeastOneSuccessfulStrategy;
import org.apache.shiro.mgt.DefaultSessionStorageEvaluator;
import org.apache.shiro.mgt.DefaultSubjectDAO;
import org.apache.shiro.mgt.SecurityManager;
import org.apache.shiro.mgt.SessionsSecurityManager;
import org.apache.shiro.realm.Realm;
import org.apache.shiro.spring.boot.autoconfigure.ShiroAutoConfiguration;
import org.apache.shiro.spring.web.ShiroFilterFactoryBean;
import org.apache.shiro.web.mgt.DefaultWebSecurityManager;
import org.springframework.aop.framework.autoproxy.DefaultAdvisorAutoProxyCreator;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.AutoConfigureBefore;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.servlet.Filter;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * @author lixiao
 * @date 2019/10/3 15:39
 */
@Configuration
@AutoConfigureBefore(value = ShiroAutoConfiguration.class)
public class ShiroConfig {

	/**
	 * 开启shiro权限注解
	 * @return DefaultAdvisorAutoProxyCreator
	 */
	@Bean
	public DefaultAdvisorAutoProxyCreator creator() {
		DefaultAdvisorAutoProxyCreator creator = new DefaultAdvisorAutoProxyCreator();
		creator.setProxyTargetClass(true);
		return creator;
	}

	/**
	 * 密码登录时使用该匹配器进行匹配
	 * @return HashedCredentialsMatcher
	 */
	@Bean("hashedCredentialsMatcher")
	public HashedCredentialsMatcher hashedCredentialsMatcher() {
		HashedCredentialsMatcher matcher = new HashedCredentialsMatcher();
		// 设置哈希算法名称
		matcher.setHashAlgorithmName("SHA-256");
		// 设置哈希迭代次数
		matcher.setHashIterations(1024);
		// 设置存储凭证十六进制编码
		matcher.setStoredCredentialsHexEncoded(true);
		return matcher;
	}

	/**
	 * 密码登录Realm
	 * @param matcher 密码匹配器
	 * @return PasswordRealm
	 */
	@Bean
	public PasswordRealm passwordRealm(@Qualifier("hashedCredentialsMatcher") HashedCredentialsMatcher matcher) {
		PasswordRealm userRealm = new PasswordRealm();
		userRealm.setCredentialsMatcher(matcher);
		return userRealm;
	}

	/**
	 * 验证码登录Realm
	 * @param matcher 密码匹配器
	 * @return CodeRealm
	 */
	@Bean
	public CodeRealm codeRealm(@Qualifier("hashedCredentialsMatcher") HashedCredentialsMatcher matcher) {
		CodeRealm codeRealm = new CodeRealm();
		codeRealm.setCredentialsMatcher(matcher);
		return codeRealm;
	}

	/**
	 * 对BearerToken 进行授权认证的 realm
	 * @return {@link OauthRealm}
	 */
	@Bean
	public OauthRealm oauthRealm() {
		return new OauthRealm();
	}

	/**
	 * Shiro内置过滤器，可以实现拦截器相关的拦截器 常用的过滤器： anon：无需认证（登录）可以访问 authc：必须认证才可以访问
	 * user：如果使用rememberMe的功能可以直接访问 perms：该资源必须得到资源权限才可以访问 role：该资源必须得到角色权限才可以访问
	 *
	 * @see org.apache.shiro.web.filter.mgt.DefaultFilter
	 **/
	@Bean
	public ShiroFilterFactoryBean shiroFilter(@Qualifier("sessionsSecurityManager") SecurityManager securityManager) {
		ShiroFilterFactoryBean bean = new ShiroFilterFactoryBean();
		// 设置 SecurityManager
		bean.setSecurityManager(securityManager);

		Map<String, String> filterMap = new LinkedHashMap<>();
		// 放行以下接口
		filterMap.put("/login/**", "anon");
		filterMap.put("/static/**", "anon");
		filterMap.put("/swagger-ui.html", "anon");
		filterMap.put("/doc.html", "anon");
		filterMap.put("/swagger-resources/**", "anon");
		filterMap.put("/v2/**", "anon");
		filterMap.put("/webjars/**", "anon");
		filterMap.put("/images/**", "anon");
		filterMap.put("/websocket/**", "anon");
		filterMap.put("/*.js", "anon");
		filterMap.put("/*.html", "anon");
		filterMap.put("/*.css", "anon");

		// 添加oauth授权过滤器
		Map<String, Filter> filter = new LinkedHashMap<>(1);
		filter.put("oauth", new OauthFilter());
		bean.setFilters(filter);

		// 其余请求需进行Bearer授权校验
		filterMap.put("/**", "oauth");
		bean.setFilterChainDefinitionMap(filterMap);
		return bean;
	}

	@Bean
	public Authenticator authenticator() {
		// 自己重写的ModularRealmAuthenticator
		CustomModularRealmAuthenticator modularRealmAuthenticator = new CustomModularRealmAuthenticator();
		modularRealmAuthenticator.setAuthenticationStrategy(new AtLeastOneSuccessfulStrategy());
		return modularRealmAuthenticator;
	}

	/**
	 * SecurityManager 是 Shiro 架构的核心，通过它来链接Realm和用户(文档中称之为Subject.)
	 */
	@Bean
	public SessionsSecurityManager sessionsSecurityManager(@Qualifier("passwordRealm") PasswordRealm passwordRealm,
			@Qualifier("codeRealm") CodeRealm codeRealm, @Qualifier("oauthRealm") OauthRealm oauthRealm,
			@Qualifier("authenticator") Authenticator authenticator) {
		DefaultWebSecurityManager securityManager = new DefaultWebSecurityManager();
		// 设置realm
		securityManager.setAuthenticator(authenticator);
		List<Realm> realms = new ArrayList<>();
		// 添加多个realm
		realms.add(passwordRealm);
		realms.add(codeRealm);
		realms.add(oauthRealm);
		securityManager.setRealms(realms);

		/*
		 * 关闭shiro自带的session
		 */
		DefaultSubjectDAO subjectDAO = new DefaultSubjectDAO();
		DefaultSessionStorageEvaluator defaultSessionStorageEvaluator = new DefaultSessionStorageEvaluator();
		defaultSessionStorageEvaluator.setSessionStorageEnabled(false);
		subjectDAO.setSessionStorageEvaluator(defaultSessionStorageEvaluator);
		securityManager.setSubjectDAO(subjectDAO);
		return securityManager;
	}

}
