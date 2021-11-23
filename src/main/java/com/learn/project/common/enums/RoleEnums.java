package com.learn.project.common.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @author knight
 */
@AllArgsConstructor
@Getter
public enum RoleEnums {

	/**
	 * 管理员
	 */
	ADMIN("1", "admin", "管理员"),
	/**
	 * 普通角色
	 */
	COMMON("2", "common", "普通角色");

	private String code;

	private String name;

	private String msg;

}
