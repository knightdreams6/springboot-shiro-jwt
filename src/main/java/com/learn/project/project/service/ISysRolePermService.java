package com.learn.project.project.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.learn.project.project.entity.SysRolePerm;

import java.util.Set;

/**
 * <p>
 * 角色和菜单关联表 服务类
 * </p>
 *
 * @author knight
 * @since 2021-11-23
 */
public interface ISysRolePermService extends IService<SysRolePerm> {

	/**
	 * 通过角色id查询权限id列表
	 * @param roleIds 角色id
	 * @return {@link Set<String>}
	 */
	Set<String> selectPermIdsByRoleIds(Set<String> roleIds);

}
