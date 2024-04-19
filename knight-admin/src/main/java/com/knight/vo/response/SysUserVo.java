package com.knight.vo.response;

import com.knight.entity.orm.SysUser;
import lombok.Data;

import java.time.LocalDate;

/**
 * sys用户vo
 *
 * @author lixiao
 * @since 2022/06/03
 */
@Data
public class SysUserVo {

	/**
	 * 用户id
	 */
	private String id;

	/**
	 * 手机号
	 */
	private String phone;

	/**
	 * 头像
	 */
	private String avatar;

	/**
	 * 用户名
	 */
	private String name;

	/**
	 * 性别0男1女
	 */
	private Integer sex;

	/**
	 * 出生日期
	 */
	private LocalDate birth;

	public SysUserVo(SysUser user) {
		this.id = user.getId();
		this.phone = user.getSuPhone();
		this.avatar = user.getSuAvatar();
		this.name = user.getSuName();
		this.sex = user.getSuSex();
		this.birth = user.getSuBirth();
	}

}
