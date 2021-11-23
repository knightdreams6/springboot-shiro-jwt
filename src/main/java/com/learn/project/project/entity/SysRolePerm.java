package com.learn.project.project.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.learn.project.framework.web.domain.BaseBean;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

/**
 * <p>
 * 角色和菜单关联表
 * </p>
 *
 * @author knight
 * @since 2021-11-23
 */
@Getter
@Setter
@TableName("sys_role_perm")
@ApiModel(value = "SysRolePerm对象", description = "角色和菜单关联表")
public class SysRolePerm extends BaseBean {

	@ApiModelProperty("主键id")
	@TableId(value = "id", type = IdType.ASSIGN_ID)
	private String id;

	@ApiModelProperty("角色ID")
	private String roleId;

	@ApiModelProperty("权限ID")
	private String permId;

}
