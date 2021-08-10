package com.learn.project.project.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * <p>
 * 菜单权限表
 * </p>
 *
 * @author lixiao
 * @since 2020-04-15
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("sys_perm")
@ApiModel(value = "权限对象", description = "权限表")
public class Perm implements Serializable {

	private static final long serialVersionUID = 1L;

	@ApiModelProperty(value = "权限ID")
	@TableId(value = "perm_id", type = IdType.AUTO)
	private Long permId;

	@ApiModelProperty(value = "菜单名称")
	private String permName;

	@ApiModelProperty(value = "菜单名称")
	private String permsKey;

	@ApiModelProperty(value = "创建时间")
	private LocalDateTime createTime;

	@ApiModelProperty(value = "更新时间")
	private LocalDateTime updateTime;

	@ApiModelProperty(value = "备注")
	private String remark;

}
