package com.learn.project.project.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.learn.project.project.entity.SysRolePerm;
import com.learn.project.project.mapper.SysRolePermMapper;
import com.learn.project.project.service.ISysRolePermService;
import org.springframework.stereotype.Service;

import java.util.Set;
import java.util.stream.Collectors;

/**
 * <p>
 * 角色和菜单关联表 服务实现类
 * </p>
 *
 * @author knight
 * @since 2021-11-23
 */
@Service
public class SysRolePermServiceImpl extends ServiceImpl<SysRolePermMapper, SysRolePerm> implements ISysRolePermService {

	@Override
	public Set<String> selectPermIdsByRoleIds(Set<String> roleIds) {
		LambdaQueryWrapper<SysRolePerm> queryWrapper = Wrappers.<SysRolePerm>lambdaQuery()
				// 查询权限id
				.select(SysRolePerm::getPermId)
				// 根据角色id过滤
				.in(SysRolePerm::getRoleId, roleIds);
		return this.list(queryWrapper).stream().map(SysRolePerm::getPermId).collect(Collectors.toSet());
	}

}
