package com.knight.entity.base;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.security.Principal;
import java.util.Set;

/**
 * @author lixiao
 * @version 1.0
 * @since 2020/4/17 14:23
 */
@Data
@NoArgsConstructor
public class LoginUser implements Serializable, Principal {

	private static final long serialVersionUID = 1L;

	/**
	 * 角色列表
	 */
	private Set<String> roleSet;

	/**
	 * 权限列表
	 */
	private Set<String> permissionsSet;

	/**
	 * 用户信息
	 */
	private UserInfo user;

	@Override
	public String getName() {
		return user.getPhone();
	}

}
