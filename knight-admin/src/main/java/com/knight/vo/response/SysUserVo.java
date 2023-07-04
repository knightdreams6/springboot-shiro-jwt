package com.knight.vo.response;

import com.knight.entity.orm.SysUser;
import io.swagger.annotations.ApiModelProperty;
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

	@ApiModelProperty(value = "用户id")
	private String id;

	@ApiModelProperty("手机号")
	private String phone;

	@ApiModelProperty("头像")
	private String avatar;

	@ApiModelProperty("用户名")
	private String name;

	@ApiModelProperty("性别0男1女")
	private Integer sex;

	@ApiModelProperty("出生日期")
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
