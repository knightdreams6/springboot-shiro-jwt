package com.learn.project.framework.shiro.realms;

import com.learn.project.common.enums.ErrorState;
import com.learn.project.common.utils.JwtUtil;
import com.learn.project.framework.shiro.token.JwtToken;
import com.learn.project.project.pojo.User;
import com.learn.project.project.service.IConUserRoleService;
import com.learn.project.project.service.IUserService;
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
 */
@Slf4j
public class JwtRealm extends AuthorizingRealm {

    @Resource
    private IUserService userService;

    @Resource
    private IConUserRoleService userRoleService;

    @Override
    public boolean supports(AuthenticationToken token) {
        return token instanceof JwtToken;
    }

    /**
     * 只有当需要检测用户权限的时候才会调用此方法，例如checkRole,checkPermission之类的
     */
    @Override
    protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principals) {
        User user = userService.selectUserByPhone(JwtUtil.getPhone(principals.toString()));

        SimpleAuthorizationInfo authorizationInfo = new SimpleAuthorizationInfo();
        // 添加角色
        authorizationInfo.addRoles(userRoleService.selectRoleNamesByUserId(user.getUserId()));
        return authorizationInfo;
    }

    @Override
    protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken auth) throws AuthenticationException {
        String token = (String) auth.getCredentials();
        // 解密获得token
        String phone = JwtUtil.getPhone(token);
        User user = userService.selectUserByPhone(phone);
        if (user == null || !JwtUtil.verify(token, user.getPassword())) {
            throw new IncorrectCredentialsException(ErrorState.TOKEN_INVALID.getMsg());
        }
        return new SimpleAuthenticationInfo(token, token, "JwtRealm");
    }
}
