package com.learn.project.common.enums;

/**
 * @author knight
 */
public enum RoleEnums {
    /**
     * 角色1
     */
    ROLE1(1, "role1", "角色1"),
    /**
     * 角色2
     */
    ROLE2(2, "role2", "角色2");

    private Integer code;

    private String name;

    private String msg;

    RoleEnums(Integer code, String name, String msg) {
        this.code = code;
        this.name = name;
        this.msg = msg;
    }

    public Integer getCode() {
        return code;
    }

    public void setCode(Integer code) {
        this.code = code;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    @Override
    public String toString() {
        return "RoleEnums{" +
                "code=" + code +
                ", name='" + name + '\'' +
                ", msg='" + msg + '\'' +
                '}';
    }
}
