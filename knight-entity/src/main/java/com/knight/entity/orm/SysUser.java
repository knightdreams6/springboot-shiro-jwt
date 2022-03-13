package com.knight.entity.orm;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.knight.entity.base.BaseBean;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

/**
 * <p>
 * 用户信息
 * </p>
 *
 * @author knight
 * @since 2021-11-23
 */
@Getter
@Setter
@TableName("sys_user")
@ApiModel(value = "SysUser对象", description = "用户信息")
public class SysUser extends BaseBean {

	@ApiModelProperty("用户id")
	@TableId(value = "id", type = IdType.ASSIGN_ID)
	private String id;

	@ApiModelProperty("手机号")
	private String suPhone;

	@ApiModelProperty("盐值")
	private String suSalt;

	@ApiModelProperty("密码")
	private String suPassword;

	@ApiModelProperty("头像")
	private String suAvatar;

	@ApiModelProperty("用户名")
	private String suName;

	@ApiModelProperty("性别0男1女")
	private Integer suSex;

	@ApiModelProperty("出生日期")
	private LocalDate suBirth;

}
