package com.learn.project.framework.config;

import com.learn.project.framework.filter.JwtFilter;
import com.learn.project.framework.shiro.realms.CodeRealm;
import com.learn.project.framework.shiro.realms.JwtRealm;
import com.learn.project.framework.shiro.realms.PasswordRealm;
import com.learn.project.framework.shiro.realms.UserModularRealmAuthenticator;
import org.apache.shiro.authc.credential.HashedCredentialsMatcher;
import org.apache.shiro.authc.pam.AtLeastOneSuccessfulStrategy;
import org.apache.shiro.mgt.DefaultSessionStorageEvaluator;
import org.apache.shiro.mgt.DefaultSubjectDAO;
import org.apache.shiro.mgt.SecurityManager;
import org.apache.shiro.realm.Realm;
import org.apache.shiro.spring.security.interceptor.AuthorizationAttributeSourceAdvisor;
import org.apache.shiro.spring.web.ShiroFilterFactoryBean;
import org.apache.shiro.web.mgt.DefaultWebSecurityManager;
import org.springframework.aop.framework.autoproxy.DefaultAdvisorAutoProxyCreator;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.servlet.Filter;
import java.util.*;

/**
 * @author lixiao
 * @date 2019/10/3 15:39
 */
@Configuration
public class ShiroConfig {

    /**
     * 开启shiro权限注解
     * @return DefaultAdvisorAutoProxyCreator
     */
    @Bean
    public static DefaultAdvisorAutoProxyCreator creator(){
        DefaultAdvisorAutoProxyCreator creator = new DefaultAdvisorAutoProxyCreator();
        creator.setProxyTargetClass(true);
        return creator;
    }

    /**
     * 开启shiro aop注解支持.
     * 使用代理方式;所以需要开启代码支持;
     *
     * @param securityManager 安全管理器
     * @return AuthorizationAttributeSourceAdvisor
     */
    @Bean
    public AuthorizationAttributeSourceAdvisor authorizationAttributeSourceAdvisor(SecurityManager securityManager) {
        AuthorizationAttributeSourceAdvisor authorizationAttributeSourceAdvisor = new AuthorizationAttributeSourceAdvisor();
        authorizationAttributeSourceAdvisor.setSecurityManager(securityManager);
        return authorizationAttributeSourceAdvisor;
    }

    /**
     * 密码登录时使用该匹配器进行匹配
     * @return HashedCredentialsMatcher
     */
    @Bean("hashedCredentialsMatcher")
    public HashedCredentialsMatcher hashedCredentialsMatcher(){
        HashedCredentialsMatcher matcher = new HashedCredentialsMatcher();
        // 设置哈希算法名称
        matcher.setHashAlgorithmName("MD5");
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
    public PasswordRealm passwordRealm(@Qualifier("hashedCredentialsMatcher") HashedCredentialsMatcher matcher){
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
    public CodeRealm codeRealm(@Qualifier("hashedCredentialsMatcher") HashedCredentialsMatcher matcher){
        CodeRealm codeRealm = new CodeRealm();
        codeRealm.setCredentialsMatcher(matcher);
        return codeRealm;
    }

    /**
     * jwtRealm
     * @return JwtRealm
     */
    @Bean
    public JwtRealm jwtRealm(){
        return new JwtRealm();
    }

    /**
     * Shiro内置过滤器，可以实现拦截器相关的拦截器
     *    常用的过滤器：
     *      anon：无需认证（登录）可以访问
     *      authc：必须认证才可以访问
     *      user：如果使用rememberMe的功能可以直接访问
     *      perms：该资源必须得到资源权限才可以访问
     *      role：该资源必须得到角色权限才可以访问
     **/
    @Bean
    public ShiroFilterFactoryBean shiroFilter(@Qualifier("securityManager") SecurityManager securityManager){
        ShiroFilterFactoryBean bean = new ShiroFilterFactoryBean();
        // 设置 SecurityManager
        bean.setSecurityManager(securityManager);
        // 设置未登录跳转url
        bean.setLoginUrl("/user/unLogin");

        Map<String, String> filterMap = new LinkedHashMap<>();
        filterMap.put("/user/**","anon");
        filterMap.put("/static/**","anon");
        filterMap.put("/user/logout", "logout");
        //从这里开始，是我为解决问题增加的，为swagger页面放行
        filterMap.put("/swagger-ui.html", "anon");
        filterMap.put("/swagger-resources/**", "anon");
        filterMap.put("/v2/**", "anon");
        filterMap.put("/webjars/**", "anon");
        filterMap.put("/images/**", "anon");

        Map<String, Filter> filter = new HashMap<>(1);
        filter.put("jwt", new JwtFilter());
        bean.setFilters(filter);
        filterMap.put("/**", "jwt");

        bean.setFilterChainDefinitionMap(filterMap);
        return bean;
    }


    @Bean
    public UserModularRealmAuthenticator userModularRealmAuthenticator(){
        //自己重写的ModularRealmAuthenticator
        UserModularRealmAuthenticator modularRealmAuthenticator = new UserModularRealmAuthenticator();
        modularRealmAuthenticator.setAuthenticationStrategy(new AtLeastOneSuccessfulStrategy());
        return modularRealmAuthenticator;
    }

    /**
     *  SecurityManager 是 Shiro 架构的核心，通过它来链接Realm和用户(文档中称之为Subject.)
     */
    @Bean
    public SecurityManager securityManager(@Qualifier("passwordRealm") PasswordRealm passwordRealm, @Qualifier("codeRealm") CodeRealm codeRealm,
                                           @Qualifier("jwtRealm") JwtRealm jwtRealm,
                                           @Qualifier("userModularRealmAuthenticator") UserModularRealmAuthenticator userModularRealmAuthenticator) {
        DefaultWebSecurityManager securityManager = new DefaultWebSecurityManager();
        // 设置realm
        securityManager.setAuthenticator(userModularRealmAuthenticator);
        List<Realm> realms = new ArrayList<>();
        // 添加多个realm
        realms.add(passwordRealm);
        realms.add(codeRealm);
        realms.add(jwtRealm);
        securityManager.setRealms(realms);

        /*
         * 关闭shiro自带的session，详情见文档
         */
        DefaultSubjectDAO subjectDAO = new DefaultSubjectDAO();
        DefaultSessionStorageEvaluator defaultSessionStorageEvaluator = new DefaultSessionStorageEvaluator();
        defaultSessionStorageEvaluator.setSessionStorageEnabled(false);
        subjectDAO.setSessionStorageEvaluator(defaultSessionStorageEvaluator);
        securityManager.setSubjectDAO(subjectDAO);

        return securityManager;
    }
}
