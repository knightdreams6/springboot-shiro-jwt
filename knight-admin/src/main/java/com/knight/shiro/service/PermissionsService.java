package com.knight.shiro.service;

import cn.hutool.core.collection.CollUtil;
import com.knight.entity.base.LoginUser;
import com.knight.entity.orm.SysRole;
import com.knight.service.ISysPermService;
import com.knight.service.ISysRoleService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * @author lixiao
 * @version 1.0
 * @date 2020/4/16 13:50
 */
@Service
@RequiredArgsConstructor
public class PermissionsService {

	/** 权限服务 */
	private final ISysPermService permService;

	/** 角色服务 */
	private final ISysRoleService roleService;

	/**
	 * 根据用户id获取其角色列表
	 * @param userId userId
	 * @return Set<Role>
	 */
	public List<SysRole> selectRoleList(String userId) {
		return roleService.selectRoleList(userId);
	}

	/**
	 * 给用户添加角色
	 * @param userId 用户id
	 * @param roleIds 角色id
	 * @return boolean
	 */
	public boolean addRole(String userId, String... roleIds) {
		return roleService.addRole(userId, roleIds);
	}

	/**
	 * 通过角色id查询权限列表
	 * @param roleIds 角色id
	 * @return {@link Set<String>}
	 */
	public Set<String> selectPermSetByRoleIds(Set<String> roleIds) {
		return permService.selectPermSetByRoleIds(roleIds);
	}

	/**
	 * 添加角色和权限
	 * @param loginUser 登录用户
	 */
	public void addRoleAndPerms(LoginUser loginUser) {
		List<SysRole> sysRoles = this.selectRoleList(loginUser.getUser().getId());
		if (CollUtil.isEmpty(sysRoles)) {
			// 填充角色
			loginUser.setRoleSet(Collections.emptySet());
			loginUser.setPermissionsSet(Collections.emptySet());
			return;
		}
		// 填充角色
		loginUser.setRoleSet(sysRoles.stream().map(SysRole::getSrKey).collect(Collectors.toSet()));
		// 构建角色id列表
		Set<String> roleIds = sysRoles.stream().map(SysRole::getId).collect(Collectors.toSet());
		// 填充权限
		loginUser.setPermissionsSet(selectPermSetByRoleIds(roleIds));
	}

}
