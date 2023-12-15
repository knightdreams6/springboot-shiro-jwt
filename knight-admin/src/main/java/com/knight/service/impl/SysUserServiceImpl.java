package com.knight.service.impl;

import cn.hutool.core.util.ObjectUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.knight.entity.enums.CommonResultConstants;
import com.knight.entity.enums.RoleEnums;
import com.knight.entity.orm.SysUser;
import com.knight.exception.ServiceException;
import com.knight.mapper.SysUserMapper;
import com.knight.service.ISysUserService;
import com.knight.shiro.service.PermissionsService;
import lombok.RequiredArgsConstructor;
import org.apache.shiro.authc.credential.HashingPasswordService;
import org.apache.shiro.crypto.hash.Hash;
import org.springframework.stereotype.Service;
import org.springframework.transaction.support.TransactionTemplate;

/**
 * <p>
 * 用户信息 服务实现类
 * </p>
 *
 * @author knight
 * @since 2021-11-23
 */
@Service
@RequiredArgsConstructor
public class SysUserServiceImpl extends ServiceImpl<SysUserMapper, SysUser> implements ISysUserService {

	/**
	 * 权限服务
	 */
	private final PermissionsService permissionsService;

	/**
	 * 事务模板
	 */
	private final TransactionTemplate transactionTemplate;

	/**
	 * 密码服务
	 */
	private final HashingPasswordService passwordService;

	@Override
	public SysUser selectUserBySubjectName(String subjectName) {
		LambdaQueryWrapper<SysUser> queryWrapper = Wrappers.<SysUser>lambdaQuery()
			.eq(SysUser::getSuPhone, subjectName)
			.or()
			.eq(SysUser::getSuMail, subjectName)
			.last("limit 1");
		return this.getOne(queryWrapper);
	}

	@Override
	public SysUser selectUserByPhone(String phone) {
		LambdaQueryWrapper<SysUser> queryWrapper = Wrappers.<SysUser>lambdaQuery()
			// 根据手机号过滤
			.eq(SysUser::getSuPhone, phone)
			.last("limit 1");
		return this.getOne(queryWrapper);
	}

	@Override
	public SysUser selectUserByMail(String mail) {
		LambdaQueryWrapper<SysUser> queryWrapper = Wrappers.<SysUser>lambdaQuery()
			.eq(SysUser::getSuMail, mail)
			.last("limit 1");
		return this.getOne(queryWrapper);
	}

	/**
	 * 用户注册,默认密码为手机号后六位
	 * @param phone phone
	 */
	@Override
	public Boolean register(String phone, String... args) {
		// 判断是否已存在该用户
		SysUser db = this.selectUserByPhone(phone);
		if (ObjectUtil.isNotNull(db)) {
			throw new ServiceException(CommonResultConstants.USER_ALREADY_EXIST);
		}
		SysUser user = new SysUser();
		user.setSuPhone(phone);
		// 如果有密码，则使用用户输入的密码
		Hash encryptPasswordHash;
		if (args.length > 0) {
			encryptPasswordHash = passwordService.hashPassword(args[0]);
		}
		else {
			encryptPasswordHash = passwordService.hashPassword(phone.substring(5, 11));
		}
		user.setSuPassword(encryptPasswordHash.toBase64());
		user.setSuSalt(encryptPasswordHash.getSalt().toBase64());
		return transactionTemplate.execute(status -> {
			try {
				this.save(user);
				return permissionsService.addRole(user.getId(), RoleEnums.ADMIN.getCode(), RoleEnums.COMMON.getCode());
			}
			catch (Exception e) {
				// 回滚
				status.setRollbackOnly();
				return Boolean.FALSE;
			}
		});
	}

}
