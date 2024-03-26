package com.knight.service.impl;

import com.knight.api.risk.component.ApiRiskUserService;
import com.knight.entity.orm.SysUser;
import com.knight.service.ISysUserService;
import lombok.RequiredArgsConstructor;
import org.apache.shiro.authc.credential.PasswordService;
import org.springframework.stereotype.Component;

import java.util.Objects;

/**
 * api风险认证用户服务
 *
 * @author knight
 * @since 2023/12/14
 */
@Component
@RequiredArgsConstructor
public class AiRiskUserServiceImpl implements ApiRiskUserService {

	/**
	 * 用户服务
	 */
	private final ISysUserService userService;

	/**
	 * 密码服务
	 */
	private final PasswordService passwordService;

	@Override
	public boolean passwordsMatch(String username, String password) {
		SysUser user = userService.selectUserBySubjectName(username);
		if (Objects.isNull(user)) {
			return false;
		}
		return passwordService.passwordsMatch(password, user.getSuPassword());
	}

}
