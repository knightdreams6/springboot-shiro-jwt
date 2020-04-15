package com.learn.project.framework.shiro.service;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTDecodeException;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.learn.project.common.constant.Constant;
import com.learn.project.project.entity.User;
import com.learn.project.project.service.IUserService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.Date;

/**
 * @author lixiao
 * @version 1.0
 * @date 2020/4/15 11:45
 */
@Service
public class TokenService {

    @Resource
    private IUserService userService;

    /**
     * 获取当前登录的User对象
     * @return User
     */
    public User getLoginUser(HttpServletRequest request){
        String token = getToken(request);
        return userService.selectUserByPhone(getPhone(token));
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
