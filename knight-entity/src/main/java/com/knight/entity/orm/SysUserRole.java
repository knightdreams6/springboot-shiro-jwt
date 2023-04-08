package com.knight.entity.orm;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

/**
 * <p>
 * 用户和角色关联表
 * </p>
 *
 * @author knight
 * @since 2021-11-23
 */
@Getter
@Setter
@TableName("sys_user_role")
@ApiModel(value = "SysUserRole对象", description = "用户和角色关联表")
public class SysUserRole {

	@TableId(value = "id", type = IdType.ASSIGN_ID)
	private String id;

	@ApiModelProperty("用户ID")
	private String userId;

	@ApiModelProperty("角色ID")
	private String roleId;

	@ApiModelProperty("创建时间")
	@TableField(fill = FieldFill.INSERT)
	private LocalDateTime createDate;

}
