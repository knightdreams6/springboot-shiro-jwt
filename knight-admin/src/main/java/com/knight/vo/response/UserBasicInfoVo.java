package com.knight.vo.response;

import com.knight.entity.base.LoginUser;
import com.knight.entity.base.UserInfo;
import io.swagger.annotations.ApiModel;
import lombok.Data;

import java.time.LocalDate;

/**
 * 用户基本信息vo
 *
 * @author knight
 * @since 2022/12/24
 */
@Data
@ApiModel(value = "用户基本信息vo")
public class UserBasicInfoVo {

	/**
	 * 电话
	 */
	private String phone;

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

	public UserBasicInfoVo(LoginUser loginUser) {
		UserInfo userInfo = loginUser.getUser();
		this.phone = userInfo.getPhone();
		this.avatar = userInfo.getAvatar();
		this.name = userInfo.getName();
		this.gender = userInfo.getGender();
		this.birth = userInfo.getBirth();
	}

}
