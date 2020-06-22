package com.learn.project.framework.shiro.service;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTDecodeException;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.learn.project.common.constant.Constant;
import com.learn.project.common.constant.RedisKey;
import com.learn.project.framework.redis.RedisCache;
import com.learn.project.framework.web.domain.LoginUser;
import com.learn.project.project.entity.User;
import com.learn.project.project.service.IUserService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.Date;
import java.util.Set;
import java.util.concurrent.TimeUnit;

/**
 * @author lixiao
 * @version 1.0
 * @date 2020/4/15 11:45
 */
@Service
public class TokenService {

    @Resource
    private IUserService userService;

    @Resource
    private PermissionsService permissionsService;

    @Resource
    private RedisCache redisCache;

    /**
     * 获取当前登录的User对象
     * @return User
     */
    public LoginUser getLoginUser(HttpServletRequest request){
        // 获取token
        String token = getToken(request);
        // 获取手机号
        String phone = getPhone(token);
        // 获取缓存loginUserKey
        String loginUserKey = RedisKey.getLoginUserKey(phone);
        // 获取缓存loginUser
        Object cacheObject = redisCache.getCacheObject(loginUserKey);
        if (cacheObject == null) {
            LoginUser loginUser = new LoginUser();
            // 获取当前登录用户
            User user = userService.selectUserByPhone(phone);
            loginUser.setUser(user);
            // 获取当前登录用户所有权限
            Set<String> permissionsSet = permissionsService.getPermissionsSet(user.getUserId());
            loginUser.setPermissionsSet(permissionsSet);
            // 获取当前登录用户所有角色
            Set<String> roleSet = permissionsService.getRoleSet(user.getUserId());
            loginUser.setRoleSet(roleSet);
            // 缓存当前登录用户
            redisCache.setCacheObject(loginUserKey, loginUser, 15, TimeUnit.MINUTES);
            return loginUser;
        }
        return (LoginUser) cacheObject;
    }


    /**
     * 获得token中的信息无需secret解密也能获得
     * @param token token
     * @return token中包含的用户手机号
     */
    public String getPhone(String token) {
        try {
            DecodedJWT jwt = JWT.decode(token);
            return jwt.getClaim("phone").asString();
        } catch (JWTDecodeException e) {
            return null;
        }
    }

    /**
     * 获得token中的信息无需secret解密也能获得
     * @param request HttpServletRequest
     * @return token中包含的用户手机号
     */
    public String getPhone(HttpServletRequest request) {
        try {
            DecodedJWT jwt = JWT.decode(this.getToken(request));
            return jwt.getClaim("phone").asString();
        } catch (JWTDecodeException e) {
            return null;
        }
    }


    /**
     * 获得token中的信息无需secret解密也能获得
     * @param token token
     * @return token中包含的用户id
     */
    public String getUserId(String token) {
        try {
            DecodedJWT jwt = JWT.decode(token);
            return jwt.getClaim("userId").asString();
        } catch (JWTDecodeException e) {
            return null;
        }
    }

    /**
     * 获得token中的信息无需secret解密也能获得
     * @param request HttpServletRequest
     * @return token中包含的用户id
     */
    public String getUserId(HttpServletRequest request) {
        try {
            DecodedJWT jwt = JWT.decode(this.getToken(request));
            return jwt.getClaim("userId").asString();
        } catch (JWTDecodeException e) {
            return null;
        }
    }


    /**
     * 获取当前登录用户的token
     * @param request HttpServletRequest
     * @return token
     */
    public String getToken(HttpServletRequest request){
        return request.getHeader(Constant.TOKEN_HEADER_NAME);
    }


    /**
     *
     * @param phone 用户名/手机号
     * @param userId   用户id
     * @param secret   用户的密码
     * @return 加密的token
     */
    public String createToken(String phone, Integer userId, String secret) {
        Date date = new Date(System.currentTimeMillis() + Constant.TOKEN_EXPIRE_TIME);
        Algorithm algorithm = Algorithm.HMAC256(secret);
        return JWT.create()
                .withClaim("phone", phone)
                .withClaim("userId", String.valueOf(userId))
                .withExpiresAt(date)
                .sign(algorithm);
    }

    /**
     * 校验token是否正确
     *
     * @param token  密钥
     * @param secret 用户的密码
     * @return 是否正确
     */
    public boolean verify(String token, String secret) {
        try {
            // 根据密码生成JWT效验器
            Algorithm algorithm = Algorithm.HMAC256(secret);
            JWTVerifier verifier = JWT.require(algorithm)
                    .withClaim("phone", getPhone(token))
                    .withClaim("userId", getUserId(token))
                    .build();
            // 效验TOKEN
            verifier.verify(token);
            return true;
        } catch (JWTVerificationException exception) {
            return false;
        }
    }

}
