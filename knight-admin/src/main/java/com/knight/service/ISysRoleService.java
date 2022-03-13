package com.knight.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.knight.entity.orm.SysRole;

import java.util.List;

/**
 * <p>
 * 角色信息 服务类
 * </p>
 *
 * @author knight
 * @since 2021-11-23
 */
public interface ISysRoleService extends IService<SysRole> {

	/**
	 * 查询角色列表
	 * @param userId 用户id
	 * @return {@link List<SysRole>}
	 */
	List<SysRole> selectRoleList(String userId);

	/**
	 * 添加角色
	 * @param userId 用户id
	 * @param roleIds 角色id列表
	 * @return boolean
	 */
	boolean addRole(String userId, String... roleIds);

}
