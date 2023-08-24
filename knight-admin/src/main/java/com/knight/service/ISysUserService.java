package com.knight.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.knight.entity.orm.SysUser;
import org.springframework.lang.Nullable;

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
	 * 根据手机号/邮箱查询用户
	 * @param subjectName 主题名称
	 * @return {@link SysUser}
	 */
	@Nullable
	SysUser selectUserBySubjectName(String subjectName);

	/**
	 * 根据手机号查询用户
	 * @param phone phone
	 * @return User
	 */
	@Nullable
	SysUser selectUserByPhone(String phone);

	/**
	 * 根据邮箱查询用户
	 * @param mail mail
	 * @return User
	 */
	@Nullable
	SysUser selectUserByMail(String mail);

	/**
	 * 用户注册,默认密码为手机号后六位
	 * @param phone phone
	 * @param args 密码
	 * @return boolean
	 */
	Boolean register(String phone, String... args);

}
