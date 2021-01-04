package com.learn.project.common.enums;

/**
 * @author lixiao
 * @date 2019/7/31 19:13
 */
public enum LoginType {
    /**
     * 密码登录
     */
    PASSWORD_LOGIN_TYPE("Password"),
    /**
     * 验证码登录
     */
    CODE_LOGIN_TYPE("Code");

    private final String type;

    LoginType(String type) {
        this.type = type;
    }

    @Override
    public String toString() {
        return this.type;
    }

}
