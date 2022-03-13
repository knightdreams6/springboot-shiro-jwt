package com.knight.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.knight.entity.orm.SysUserRole;
import com.knight.mapper.SysUserRoleMapper;
import com.knight.service.ISysUserRoleService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * <p>
 * 用户和角色关联表 服务实现类
 * </p>
 *
 * @author knight
 * @since 2021-11-23
 */
@Service
public class SysUserRoleServiceImpl extends ServiceImpl<SysUserRoleMapper, SysUserRole> implements ISysUserRoleService {

	@Override
	public Set<String> selectRoleIdByUserId(String userId) {
		LambdaQueryWrapper<SysUserRole> queryWrapper = Wrappers.<SysUserRole>lambdaQuery()
				// 查询角色id
				.select(SysUserRole::getRoleId)
				// 根据用户id
				.eq(SysUserRole::getUserId, userId);
		List<SysUserRole> userRoles = this.list(queryWrapper);
		return userRoles.stream().map(SysUserRole::getRoleId).collect(Collectors.toSet());
	}

}
