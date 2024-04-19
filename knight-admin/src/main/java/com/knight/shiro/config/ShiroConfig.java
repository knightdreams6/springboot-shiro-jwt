package com.knight.shiro.config;

import com.knight.shiro.filter.OauthFilter;
import com.knight.shiro.realms.*;
import jakarta.servlet.Filter;
import org.apache.shiro.authc.Authenticator;
import org.apache.shiro.authc.credential.DefaultPasswordService;
import org.apache.shiro.authc.credential.HashedCredentialsMatcher;
import org.apache.shiro.authc.credential.PasswordMatcher;
import org.apache.shiro.authc.credential.PasswordService;
import org.apache.shiro.authc.pam.AtLeastOneSuccessfulStrategy;
import org.apache.shiro.crypto.hash.Sha256Hash;
import org.apache.shiro.mgt.SecurityManager;
import org.apache.shiro.mgt.*;
import org.apache.shiro.realm.Realm;
import org.apache.shiro.spring.config.web.autoconfigure.ShiroWebAutoConfiguration;
import org.apache.shiro.spring.web.ShiroFilterFactoryBean;
import org.apache.shiro.web.mgt.DefaultWebSecurityManager;
import org.springframework.aop.framework.autoproxy.DefaultAdvisorAutoProxyCreator;
import org.springframework.boot.autoconfigure.AutoConfigureBefore;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * shiro配置类
 *
 * @author kinght
 * @see org.apache.shiro.spring.config.web.autoconfigure.ShiroWebAutoConfiguration
 */
@Configuration
@AutoConfigureBefore(value = ShiroWebAutoConfiguration.class)
public class ShiroConfig {

	/**
	 * 开启shiro权限注解
	 * @return DefaultAdvisorAutoProxyCreator
	 */
	@Bean
	public DefaultAdvisorAutoProxyCreator creator() {
		var creator = new DefaultAdvisorAutoProxyCreator();
		creator.setProxyTargetClass(true);
		return creator;
	}

	/**
	 * Hash密码服务 默认使用 argon2id
	 * @return HashingPasswordService
	 */
	@Bean
	public PasswordService passwordService() {
		return new DefaultPasswordService();
	}

	/**
	 * HashedCredentialsMatcher
	 * @return HashedCredentialsMatcher
	 */
	@Bean
	public HashedCredentialsMatcher codeCredentialsMatcher() {
		var matcher = new HashedCredentialsMatcher();
		matcher.setHashAlgorithmName(Sha256Hash.ALGORITHM_NAME);
		return matcher;
	}

	/**
	 * 密码登录Realm
	 * @param passwordService 密码服务
	 * @return PasswordRealm
	 */
	@Bean
	public PasswordRealm passwordRealm(PasswordService passwordService) {
		var userRealm = new PasswordRealm();
		PasswordMatcher passwordMatcher = new PasswordMatcher();
		passwordMatcher.setPasswordService(passwordService);
		userRealm.setCredentialsMatcher(passwordMatcher);
		return userRealm;
	}

	/**
	 * 验证码登录Realm
	 * @param codeCredentialsMatcher 验证码凭据匹配器
	 * @return CodeRealm
	 */
	@Bean
	public PhoneCodeRealm codeRealm(HashedCredentialsMatcher codeCredentialsMatcher) {
		var codeRealm = new PhoneCodeRealm();
		codeRealm.setCredentialsMatcher(codeCredentialsMatcher);
		return codeRealm;
	}

	/**
	 * 邮箱验证码登录Realm
	 * @param codeCredentialsMatcher 验证码凭据匹配器
	 * @return {@link MailCodeRealm}
	 */
	@Bean
	public MailCodeRealm mailCodeRealm(HashedCredentialsMatcher codeCredentialsMatcher) {
		var mailCodeRealm = new MailCodeRealm();
		mailCodeRealm.setCredentialsMatcher(codeCredentialsMatcher);
		return mailCodeRealm;
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
	public ShiroFilterFactoryBean shiroFilterFactoryBean(SecurityManager securityManager) {
		var bean = new ShiroFilterFactoryBean();
		// 设置 SecurityManager
		bean.setSecurityManager(securityManager);

		Map<String, String> filterMap = new LinkedHashMap<>();
		// 放行以下接口
		filterMap.put("/login/**", "anon");
		filterMap.put("/static/**", "anon");
		filterMap.put("/swagger-ui.html", "anon");
		filterMap.put("/doc.html", "anon");
		filterMap.put("/swagger-resources/**", "anon");
		filterMap.put("/webjars/**", "anon");
		filterMap.put("/v3/api-docs/**", "anon");
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

	/**
	 * 身份验证器
	 * @return Authenticator
	 */
	@Bean
	public Authenticator authenticator() {
		var modularRealmAuthenticator = new CustomModularRealmAuthenticator();
		modularRealmAuthenticator.setAuthenticationStrategy(new AtLeastOneSuccessfulStrategy());
		return modularRealmAuthenticator;
	}

	/**
	 * session存储
	 * @return SessionStorageEvaluator
	 */
	@Bean
	public SessionStorageEvaluator sessionStorageEvaluator() {
		var defaultSessionStorageEvaluator = new DefaultSessionStorageEvaluator();
		// 无状态应用不存储session
		defaultSessionStorageEvaluator.setSessionStorageEnabled(false);
		return defaultSessionStorageEvaluator;
	}

	/**
	 * SubjectDAO
	 * @param sessionStorageEvaluator sessionStorageEvaluator
	 * @return SubjectDAO
	 */
	@Bean
	public SubjectDAO subjectDAO(SessionStorageEvaluator sessionStorageEvaluator) {
		var defaultSubjectDAO = new DefaultSubjectDAO();
		// 无状态应用不存储session
		defaultSubjectDAO.setSessionStorageEvaluator(sessionStorageEvaluator);
		return defaultSubjectDAO;
	}

	/**
	 * SecurityManager
	 * @return SessionsSecurityManager
	 */
	@Bean
	public SessionsSecurityManager securityManager(List<Realm> realms, Authenticator authenticator,
			SubjectDAO subjectDAO) {
		var securityManager = new DefaultWebSecurityManager();
		// 设置自定义的身份验证器
		securityManager.setAuthenticator(authenticator);

		// 设置 realms
		securityManager.setRealms(realms);

		securityManager.setSubjectDAO(subjectDAO);
		return securityManager;
	}

}
