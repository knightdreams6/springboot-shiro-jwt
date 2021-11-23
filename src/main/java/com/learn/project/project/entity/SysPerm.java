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
 * 菜单权限表
 * </p>
 *
 * @author knight
 * @since 2021-11-23
 */
@Getter
@Setter
@TableName("sys_perm")
@ApiModel(value = "SysPerm对象", description = "菜单权限表")
public class SysPerm extends BaseBean {

	@ApiModelProperty("权限id")
	@TableId(value = "id", type = IdType.ASSIGN_ID)
	private String id;

	@ApiModelProperty("权限名称")
	private String spName;

	@ApiModelProperty("权限标识")
	private String spKey;

	@ApiModelProperty("备注")
	private String spRemark;

}
