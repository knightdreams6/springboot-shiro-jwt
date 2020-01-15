package com.learn.project.common.constant;

/**
 * 常量类
 * @author lixiao
 * @date 2019/10/3 15:45
 */
public class Constant {

    /**
     * 验证码过期时间 此处为五分钟
     */
    public static Integer CODE_EXPIRE_TIME = 60 * 5;

    /**
     * jwtToken过期时间
     */
    public static Long TOKEN_EXPIRE_TIME = 30 * 24 * 60 * 60 * 1000L;

    /**
     * token请求头名称
     */
    public static String TOKEN_HEADER_NAME = "authorization";
}
