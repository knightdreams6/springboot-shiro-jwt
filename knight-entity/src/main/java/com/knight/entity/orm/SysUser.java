package com.knight.entity.orm;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.knight.entity.base.BaseBean;
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
public class SysUser extends BaseBean {

	/**
	 * 用户id
	 */
	@TableId(value = "id", type = IdType.ASSIGN_ID)
	private String id;

	/**
	 * 手机号
	 */
	private String suPhone;

	/**
	 * 密码
	 */
	private String suPassword;

	/**
	 * 头像
	 */
	private String suAvatar;

	/**
	 * 用户名
	 */
	private String suName;

	/**
	 * 性别0女1男
	 */
	private Integer suSex;

	/**
	 * 出生日期
	 */
	private LocalDate suBirth;

	/**
	 * 邮箱
	 */
	private String suMail;

}
