package com.knight.service.impl;

import com.knight.api.risk.component.ApiRiskUserService;
import com.knight.entity.orm.SysUser;
import com.knight.service.ISysUserService;
import lombok.RequiredArgsConstructor;
import org.apache.shiro.authc.credential.DefaultPasswordService;
import org.apache.shiro.authc.credential.HashingPasswordService;
import org.apache.shiro.codec.Base64;
import org.apache.shiro.crypto.hash.Sha256Hash;
import org.apache.shiro.util.ByteSource;
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
	private final HashingPasswordService passwordService;

	@Override
	public boolean passwordsMatch(String username, String password) {
		SysUser user = userService.selectUserBySubjectName(username);
		if (Objects.isNull(user)) {
			return false;
		}

		Sha256Hash sha256Hash = Sha256Hash.fromHexString(user.getSuPassword());
		sha256Hash.setSalt(ByteSource.Util.bytes(Base64.decode(user.getSuSalt())));
		sha256Hash.setIterations(DefaultPasswordService.DEFAULT_HASH_ITERATIONS);
		return passwordService.passwordsMatch(password, sha256Hash);
	}

}
