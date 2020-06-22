package com.learn.project.framework.shiro.realms;

import com.learn.project.common.enums.ErrorState;
import com.learn.project.common.utils.ServletUtils;
import com.learn.project.framework.shiro.service.TokenService;
import com.learn.project.framework.shiro.token.JwtToken;
import com.learn.project.framework.web.domain.LoginUser;
import com.learn.project.project.entity.User;
import com.learn.project.project.service.IUserService;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.authc.*;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;
import org.springframework.util.StringUtils;

import javax.annotation.Resource;

/**
 * @author lixiao
 * @date 2019/8/6 10:02
 */
@Slf4j
public class JwtRealm extends AuthorizingRealm {

    @Resource
    private TokenService tokenService;

    @Resource
    private IUserService userService;

    @Override
    public boolean supports(AuthenticationToken token) {
        return token instanceof JwtToken;
    }

    /**
     * 只有当需要检测用户权限的时候才会调用此方法，例如checkRole,checkPermission之类的
     */
    @Override
    protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principals) {
        LoginUser loginUser = tokenService.getLoginUser(ServletUtils.getRequest());
        SimpleAuthorizationInfo authorizationInfo = new SimpleAuthorizationInfo();
        // 添加角色
        authorizationInfo.addRoles(loginUser.getRoleSet());
        // 添加权限
        authorizationInfo.addStringPermissions(loginUser.getPermissionsSet());
        return authorizationInfo;
    }

    @Override
    protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken auth) throws AuthenticationException {
        String token = (String) auth.getCredentials();
        // 获得phone
        String phone = tokenService.getPhone(token);
        log.info(phone + " - token auth start...");
        if (StringUtils.isEmpty(phone)) {
            throw new IncorrectCredentialsException();
        }
        User user = userService.selectUserByPhone(phone);
        if (user == null) {
            throw new IncorrectCredentialsException();
        }
        try{
            boolean verify = tokenService.verify(token, user.getPassword());
            if(verify == Boolean.FALSE){
                throw new IncorrectCredentialsException();
            }
        } catch (Exception e){
            throw new IncorrectCredentialsException();
        }
        return new SimpleAuthenticationInfo(token, token, "JwtRealm");
    }
}
