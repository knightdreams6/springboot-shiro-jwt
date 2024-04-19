package com.knight.entity.base;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableLogic;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

/**
 * 基础bean
 *
 * @author knight
 * @since 2021/11/23
 */
@Getter
@Setter
public class BaseBean {

	/**
	 * 创建时间
	 */
	@TableField(fill = FieldFill.INSERT)
	private LocalDateTime createDate;

	/**
	 * 更新时间
	 */
	@TableField(fill = FieldFill.UPDATE)
	private LocalDateTime updateDate;

	/**
	 * 删除时间
	 */
	@TableField(select = false, fill = FieldFill.UPDATE)
	private LocalDateTime deleteDate;

	/**
	 * 是否删除0否1是
	 */
	@TableLogic
	@TableField(select = false, fill = FieldFill.UPDATE)
	private Integer deleted;

}
