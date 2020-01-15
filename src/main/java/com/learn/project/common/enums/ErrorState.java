package com.learn.project.common.enums;

/**
 * @author lixiao
 * @date 2019/8/5 17:16
 */
public enum ErrorState {

    // 系统错误状态码
    SYSTEM_ERROR(1, "系统异常"),
    // Redis ERROR
    REDIS_CONNECTION_FAILURE(2, "redis操作失败，请检查链接"),
    // 发送短信失败
    SEND_SMS_ERROR(3, "发送失败"),
    // 该用户不存在
    USER_NOT_EXIST(101, "用户不存在"),
    // 验证码无效
    CODE_EXPIRE(102, "验证码无效"),
    // 验证码不正确
    CODE_ERROR(103, "验证码不正确"),
    // 用户名不存在
    USERNAME_NOT_EXIST(104, "用户名不存在"),
    // 密码不正确
    PASSWORD_ERROR(105, "密码不正确"),
    // 没有相关权限
    NOT_AUTH(106, "没有相关权限"),
    // token无效
    TOKEN_INVALID(107, "token无效或已过期"),
    // 缺少相应参数
    MISSING_PARAMETER(108, "参数绑定失败:缺少参数或参数类型不正确");


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
    }}
