package com.knight.service.impl;

import cn.hutool.core.collection.CollUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.knight.entity.orm.SysPerm;
import com.knight.mapper.SysPermMapper;
import com.knight.service.ISysPermService;
import com.knight.service.ISysRolePermService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * <p>
 * 菜单权限表 服务实现类
 * </p>
 *
 * @author knight
 * @since 2021-11-23
 */
@Service
@RequiredArgsConstructor
public class SysPermServiceImpl extends ServiceImpl<SysPermMapper, SysPerm> implements ISysPermService {

	/** 角色权限服务 */
	private final ISysRolePermService rolePermService;

	@Override
	public Set<String> selectPermSetByRoleIds(Set<String> roleIds) {
		Set<String> permIds = rolePermService.selectPermIdsByRoleIds(roleIds);
		if (CollUtil.isEmpty(permIds)) {
			return Collections.emptySet();
		}
		LambdaQueryWrapper<SysPerm> queryWrapper = Wrappers.<SysPerm>lambdaQuery()
				// 查询权限值
				.select(SysPerm::getSpKey)
				// 根据权限id列表
				.in(SysPerm::getId, permIds);
		return this.list(queryWrapper).stream().map(SysPerm::getSpKey).collect(Collectors.toSet());
	}

}
