package com.learn.project.framework.web.domain;

import com.learn.project.project.entity.SysUser;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;
import java.time.LocalDate;

/**
 * 用户信息
 *
 * @author knight
 * @date 2021/11/23
 */
@NoArgsConstructor
@Getter
@Setter
public class UserInfo implements Serializable {

	private static final long serialVersionUID = 1L;

	/** id */
	private String id;

	/** 电话 */
	private String phone;

	/** 头像 */
	private String avatar;

	/** 姓名 */
	private String name;

	/** 性别 */
	private Integer sex;

	/** 出生日期 */
	private LocalDate birth;

	public UserInfo(SysUser user) {
		this.id = user.getId();
		this.phone = user.getSuPhone();
		this.avatar = user.getSuAvatar();
		this.name = user.getSuName();
		this.sex = user.getSuSex();
		this.birth = user.getSuBirth();
	}

}
