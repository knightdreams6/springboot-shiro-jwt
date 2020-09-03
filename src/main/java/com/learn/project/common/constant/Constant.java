package com.learn.project.common.constant;

import java.util.concurrent.TimeUnit;

/**
 * 常量类
 * @author lixiao
 * @date 2019/10/3 15:45
 */
public class Constant {

    /**
     * 验证码过期时间 此处为五分钟
     */
    public static Integer CODE_EXPIRE_TIME = 5;

    /**
     * jwtToken过期时间
     */
    public static Long TOKEN_EXPIRE_TIME = TimeUnit.DAYS.toMillis(30);

    /**
     * token请求头名称
     */
    public static String TOKEN_HEADER_NAME = "X-Token";

    /**
     * 表单重复提交间隔时间 单位 S
     * 两次相同参数的请求，如果间隔时间大于该参数，系统不会认定为重复提交的数据
     */
    public static int FORM_REPEAT_TIME = 10;
}
