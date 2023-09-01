package com.knight.entity.base;

import com.knight.entity.orm.SysUser;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;
import java.time.LocalDate;

/**
 * 用户信息
 *
 * @author knight
 * @since 2021/11/23
 */
@NoArgsConstructor
@Getter
@Setter
public class UserInfo implements Serializable {

	private static final long serialVersionUID = 1L;

	/**
	 * id
	 */
	private String id;

	/**
	 * 电话
	 */
	private String phone;

	/**
	 * 邮箱
	 */
	private String mail;

	/**
	 * 头像
	 */
	private String avatar;

	/**
	 * 姓名
	 */
	private String name;

	/**
	 * 性别
	 */
	private Integer gender;

	/**
	 * 出生日期
	 */
	private LocalDate birth;

	public UserInfo(SysUser user) {
		this.id = user.getId();
		this.phone = user.getSuPhone();
		this.mail = user.getSuMail();
		this.avatar = user.getSuAvatar();
		this.name = user.getSuName();
		this.gender = user.getSuSex();
		this.birth = user.getSuBirth();
	}

}
