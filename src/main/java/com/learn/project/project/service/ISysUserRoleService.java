package com.learn.project.project.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.learn.project.project.entity.SysUserRole;

import java.util.Set;

/**
 * <p>
 * 用户和角色关联表 服务类
 * </p>
 *
 * @author knight
 * @since 2021-11-23
 */
public interface ISysUserRoleService extends IService<SysUserRole> {

	/**
	 * 通过用户id查询角色id列表
	 * @param userId 用户id
	 * @return {@link Set<String>}
	 */
	Set<String> selectRoleIdByUserId(String userId);

}
