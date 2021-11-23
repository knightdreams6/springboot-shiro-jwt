package com.learn.project.project.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.learn.project.project.entity.SysUser;

/**
 * <p>
 * 用户信息 服务类
 * </p>
 *
 * @author knight
 * @since 2021-11-23
 */
public interface ISysUserService extends IService<SysUser> {

	/**
	 * 根据手机号查询用户
	 * @param phone phone
	 * @return User
	 */
	SysUser selectUserByPhone(String phone);

	/**
	 * 用户注册,默认密码为手机号后六位
	 * @param phone phone
	 * @param args 密码
	 * @return boolean
	 */
	Boolean register(String phone, String... args);

}
