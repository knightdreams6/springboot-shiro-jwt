package com.learn.project.common.constant;

/**
 * @author lixiao
 * @date 2019/10/26 12:03
 */
public class RedisKey {

    public static String getLoginCodeKey(String phone){
        return "LOGIN:CODE:" + phone;
    }

    public static String getModifyPasswordCodeKey(String phone){
        return "MODIFY:PASSWORD:CODE:" + phone;
    }

    public static String getUserKey(String phone){
        return "USER:" + phone;
    }

    public static String getLoginUserKey(String phone){
        return "LOGIN:USER:" + phone;
    }

    public static String getFormRepeatKey(){
        return "REPEAT:DATA";
    }

}
