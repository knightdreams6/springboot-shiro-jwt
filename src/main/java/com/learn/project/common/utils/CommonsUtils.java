package com.learn.project.common.utils;

import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.crypto.hash.SimpleHash;

import java.util.UUID;


/**
 * @author lixiao
 * @date 2019/7/31 17:11
 */
@Slf4j
public class CommonsUtils {

    /**
     * 获取六位数验证码
     * @return 验证码
     */
    public static int getCode(){
        return (int)((Math.random()*9+1)*1000);
    }


    /**
     * 使用md5加密
     * @param password 需要加密的密码
     * @param salt 盐
     * @return 返回加密后的密码
     */
    public static String encryptPassword(String password, String salt){
        return String.valueOf(new SimpleHash("MD5", password,  salt, 1024));
    }


    /**
     * 获取请求域中的UserId
     */
    public static Integer getUserId(){
        return (Integer) ServletUtils.getRequest().getAttribute("userId");
    }


    /**
     * 获取uuid
     * @return string
     */
    public static String uuid(){
        return UUID.randomUUID().toString().replace("-", "");
    }


}
