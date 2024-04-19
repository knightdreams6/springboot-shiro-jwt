package com.knight.entity.orm;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.knight.entity.base.BaseBean;
import lombok.Getter;
import lombok.Setter;

/**
 * <p>
 * 角色信息
 * </p>
 *
 * @author knight
 * @since 2021-11-23
 */
@Getter
@Setter
@TableName("sys_role")
public class SysRole extends BaseBean {

	/**
	 * 角色id
	 */
	@TableId(value = "id", type = IdType.ASSIGN_ID)
	private String id;

	/**
	 * 角色名称
	 */
	private String srName;

	/**
	 * 角色权限字符串
	 */
	private String srKey;

	/**
	 * 备注
	 */
	private String srRemark;

}
