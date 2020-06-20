package com.learn.project.common.enums;

/**
 * @author lixiao
 * @date 2019/8/5 17:16
 * 本项目异常使用四位数
 * 第三方接口异常使用五位数
 */
public enum ErrorState {

    // 系统错误状态码
    GENERAL_EXCEPTION(1000, "通用异常"),

    SEND_SMS_ERROR(10001, "短信发送失败"),
    // 该用户已存在
    USER_ALREADY_EXIST(1001, "用户名已存在"),
    // 验证码无效
    CODE_EXPIRE(1002, "验证码无效"),
    // 验证码不正确
    CODE_ERROR(1003, "验证码不正确"),
    // 用户名不存在
    USERNAME_NOT_EXIST(1004, "用户名不存在"),
    // 密码不正确
    PASSWORD_ERROR(1005, "密码不正确"),
    // 没有相关权限
    NOT_AUTH(1006, "没有相关权限"),
    // token无效
    TOKEN_INVALID(1007, "token failure!"),
    // 缺少相应参数
    MISSING_PARAMETER(1008, "参数绑定失败:缺少参数")
    ;


    private Integer code;
    private String msg;

    ErrorState(Integer code, String msg) {
        this.code = code;
        this.msg = msg;
    }

    public Integer getCode() {
        return code;
    }

    public String getMsg() {
        return msg;
    }

    @Override
    public String toString() {
        return "ResultEnums{" +
                "code=" + code +
                ", msg='" + msg + '\'' +
                '}';
    }
}
