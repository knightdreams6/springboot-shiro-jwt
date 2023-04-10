package com.knight.service.impl;

import cn.hutool.core.collection.CollUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.knight.entity.orm.SysRole;
import com.knight.entity.orm.SysUserRole;
import com.knight.mapper.SysRoleMapper;
import com.knight.service.ISysRoleService;
import com.knight.service.ISysUserRoleService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * <p>
 * 角色信息 服务实现类
 * </p>
 *
 * @author knight
 * @since 2021-11-23
 */
@Service
@RequiredArgsConstructor
public class SysRoleServiceImpl extends ServiceImpl<SysRoleMapper, SysRole> implements ISysRoleService {

	/**
	 * 用户角色服务
	 */
	private final ISysUserRoleService userRoleService;

	@Override
	public List<SysRole> selectRoleList(String userId) {
		// 角色id列表
		Set<String> roleIds = userRoleService.selectRoleIdByUserId(userId);
		if (CollUtil.isEmpty(roleIds)) {
			return Collections.emptyList();
		}
		LambdaQueryWrapper<SysRole> queryWrapper = Wrappers.<SysRole>lambdaQuery()
			// 查询角色id,name,key
			.select(SysRole::getId, SysRole::getSrName, SysRole::getSrKey)
			// 根据角色id列表过滤
			.in(SysRole::getId, roleIds);
		return this.list(queryWrapper);
	}

	@Override
	public boolean addRole(String userId, String... roleIds) {
		List<SysUserRole> userRoles = Arrays.stream(roleIds).map(roleId -> {
			SysUserRole userRole = new SysUserRole();
			userRole.setUserId(userId);
			userRole.setRoleId(roleId);
			return userRole;
		}).collect(Collectors.toList());
		return userRoleService.saveBatch(userRoles);
	}

}
