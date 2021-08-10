package com.learn.project.framework.shiro.service;

import com.learn.project.project.mapper.PermMapper;
import com.learn.project.project.mapper.RoleMapper;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.HashSet;
import java.util.Set;

/**
 * @author lixiao
 * @version 1.0
 * @date 2020/4/16 13:50
 */
@Service
public class PermissionsService {

	@Resource
	private PermMapper menuMapper;

	@Resource
	private RoleMapper roleMapper;

	/**
	 * 根据用户id获取其角色列表
	 * @param userId userId
	 * @return Set<Role>
	 */
	public Set<String> getRoleSet(Integer userId) {
		return roleMapper.getRoleSet(userId);
	}

	/**
	 * 给用户添加角色
	 */
	public void addRole(Integer userId, Integer... roleIds) {
		roleMapper.addRole(userId, roleIds);
	}

	/**
	 * 根据用户id获取权限列表
	 * @param userId userId
	 * @return Set<Role>
	 */
	public Set<String> getPermissionsSet(Integer userId) {
		Set<Integer> roleIdSet = roleMapper.getRoleIdSet(userId);
		Set<String> strings = new HashSet<>();
		for (Integer roleId : roleIdSet) {
			Set<String> permissionsSetByRoleId = getPermissionsSetByRoleId(roleId);
			strings.addAll(permissionsSetByRoleId);
		}
		// 去除空权限
		strings.remove("");
		return strings;
	}

	/**
	 * 根据角色id获取权限列表
	 * @param roleId 角色id
	 * @return 权限列表
	 */
	public Set<String> getPermissionsSetByRoleId(Integer roleId) {
		return menuMapper.getPermissionsSet(roleId);
	}

}
