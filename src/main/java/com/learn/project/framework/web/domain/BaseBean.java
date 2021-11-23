package com.learn.project.framework.web.domain;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableLogic;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

/**
 * 基础bean
 *
 * @author knight
 * @date 2021/11/23
 */
@Getter
@Setter
public class BaseBean {

	@ApiModelProperty("创建时间")
	@TableField(fill = FieldFill.INSERT)
	private LocalDateTime createDate;

	@ApiModelProperty("更新时间")
	@TableField(fill = FieldFill.UPDATE)
	private LocalDateTime updateDate;

	@ApiModelProperty("删除时间")
	@TableField(select = false, fill = FieldFill.UPDATE)
	private LocalDateTime deleteDate;

	@TableLogic
	@TableField(select = false, fill = FieldFill.UPDATE)
	@ApiModelProperty(value = "是否删除0否1是")
	private Integer deleted;

}
