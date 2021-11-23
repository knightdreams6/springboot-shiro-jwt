package com.learn.project.project.service.impl;

import cn.hutool.core.util.IdUtil;
import cn.hutool.core.util.ObjectUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.learn.project.common.enums.ErrorState;
import com.learn.project.common.enums.RoleEnums;
import com.learn.project.common.utils.CommonsUtils;
import com.learn.project.framework.shiro.service.PermissionsService;
import com.learn.project.framework.web.exception.ServiceException;
import com.learn.project.project.entity.SysUser;
import com.learn.project.project.mapper.SysUserMapper;
import com.learn.project.project.service.ISysUserService;
import lombok.RequiredArgsConstructor;
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

	private final PermissionsService permissionsService;

	private final TransactionTemplate transactionTemplate;

	@Override
	public SysUser selectUserByPhone(String phone) {
		LambdaQueryWrapper<SysUser> queryWrapper = Wrappers.<SysUser>lambdaQuery()
				// 根据手机号过滤
				.eq(SysUser::getSuPhone, phone).last("limit 1");
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
			throw new ServiceException(ErrorState.USER_ALREADY_EXIST);
		}
		SysUser user = new SysUser();
		user.setSuPhone(phone);
		// 如果有密码，则使用用户输入的密码
		String salt = IdUtil.simpleUUID();
		String encryptPassword;
		if (args.length > 0) {
			encryptPassword = CommonsUtils.encryptPassword(args[0], salt);
		}
		else {
			encryptPassword = CommonsUtils.encryptPassword(phone.substring(5, 11), salt);
		}
		user.setSuPassword(encryptPassword);
		user.setSuSalt(salt);
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
