package com.learn.project.project.entity;

import com.baomidou.mybatisplus.annotation.*;
import com.fasterxml.jackson.annotation.JsonIgnore;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * <p>
 *
 * </p>
 *
 * @author knight
 * @since 2020-03-28
 */
@Data
@Accessors(chain = true)
@TableName("sys_user")
@ApiModel(value="User对象", description="用户信息")
@NoArgsConstructor
public class User implements Serializable {

    private static final long serialVersionUID=1L;

    @ApiModelProperty(value = "用户id")
    @TableId(value = "user_id", type = IdType.AUTO)
    private Integer userId;

    @ApiModelProperty(value = "手机号")
    private String phone;

    @ApiModelProperty(value = "盐值", hidden = true)
    @JsonIgnore
    private String salt;

    @ApiModelProperty(value = "密码", hidden = true)
    @JsonIgnore
    private String password;

    @ApiModelProperty(value = "头像")
    private String avatar;

    @ApiModelProperty(value = "用户名")
    private String name;

    @ApiModelProperty(value = "性别")
    private Integer sex;

    @ApiModelProperty(value = "出生日期")
    private LocalDate birth;

    @ApiModelProperty(value = "注册时间")
    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime createTime;

    @ApiModelProperty(value = "更新时间", hidden = true)
    @TableField(fill = FieldFill.UPDATE)
    @JsonIgnore
    private LocalDateTime updateTime;

    @ApiModelProperty(value = "0未删除1已删除" ,hidden = true)
    @JsonIgnore
    private Integer flag;

}
