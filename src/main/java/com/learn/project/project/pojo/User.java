package com.learn.project.project.pojo;

import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.IdType;
import java.time.LocalDate;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * <p>
 *  用户
 * </p>
 *
 * @author knight
 * @since 2019-12-17
 */
@TableName("project_user")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class User implements Serializable {

    private static final long serialVersionUID=1L;

    /**
     * 用户id
     */
    @TableId(value = "user_id", type = IdType.AUTO)
    private Integer userId;

    /**
     * 手机号
     */
    private String phone;

    /**
     * 密码
     */
    private String password;

    /**
     * 用户名
     */
    private String name;

    /**
     * 性别
     */
    private Integer sex;

    /**
     * 出生日期
     */
    private LocalDate birth;

    /**
     * 注册日期
     */
    private LocalDateTime registerTime;
}
