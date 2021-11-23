package com.learn.project.project.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.learn.project.project.entity.SysPerm;

import java.util.Set;

/**
 * <p>
 * 菜单权限表 服务类
 * </p>
 *
 * @author knight
 * @since 2021-11-23
 */
public interface ISysPermService extends IService<SysPerm> {

	/**
	 * 通过角色id查询权限列表
	 * @param roleIds 角色id
	 * @return {@link Set<String>}
	 */
	Set<String> selectPermSetByRoleIds(Set<String> roleIds);

}
