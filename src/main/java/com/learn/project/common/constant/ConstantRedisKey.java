package com.learn.project.common.constant;

/**
 * @author lixiao
 * @date 2019/10/26 12:03
 */
public class ConstantRedisKey {


    public static String getLoginCodeKey(String phone){
        return "LOGIN:CODE:" + phone;
    }

    public static String getModifyPasswordCodeKey(String phone){
        return "MODIFY:PASSWORD:CODE:" + phone;
    }

}
