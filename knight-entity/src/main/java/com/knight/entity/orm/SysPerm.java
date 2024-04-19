package com.knight.entity.orm;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.knight.entity.base.BaseBean;
import lombok.Getter;
import lombok.Setter;

/**
 * <p>
 * 菜单权限表
 * </p>
 *
 * @author knight
 * @since 2021-11-23
 */
@Getter
@Setter
@TableName("sys_perm")
public class SysPerm extends BaseBean {

	/**
	 * 权限id
	 */
	@TableId(value = "id", type = IdType.ASSIGN_ID)
	private String id;

	/**
	 * 权限名称
	 */
	private String spName;

	/**
	 * 权限标识
	 */
	private String spKey;

	/**
	 * 备注
	 */
	private String spRemark;

}
