package com.learn.project.framework.web.domain;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.Set;

/**
 * @author lixiao
 * @version 1.0
 * @date 2020/4/17 14:23
 */
@Data
@NoArgsConstructor
public class LoginUser implements Serializable {

	private static final long serialVersionUID = 1L;

	/** 角色列表 */
	private Set<String> roleSet;

	/** 权限列表 */
	private Set<String> permissionsSet;

	/** 用户信息 */
	private UserInfo user;

}
