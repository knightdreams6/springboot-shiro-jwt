package com.knight.vo.response;

import com.knight.api.version.annotation.ApiVersionFiled;
import com.knight.entity.base.LoginUser;
import com.knight.entity.base.UserInfo;
import lombok.Data;

import java.time.LocalDate;
import java.util.Set;

/**
 * 用户基本信息vo
 *
 * @author knight
 * @since 2022/12/24
 */
@Data
public class UserBasicInfoVo {

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

	/**
	 * 角色列表（V2版本才返回）
	 */
	@ApiVersionFiled(2)
	private Set<String> roleSet;

	public UserBasicInfoVo(LoginUser loginUser) {
		UserInfo userInfo = loginUser.getUser();
		this.phone = userInfo.getPhone();
		this.mail = userInfo.getMail();
		this.avatar = userInfo.getAvatar();
		this.name = userInfo.getName();
		this.gender = userInfo.getGender();
		this.birth = userInfo.getBirth();
		this.roleSet = loginUser.getRoleSet();
	}

}
