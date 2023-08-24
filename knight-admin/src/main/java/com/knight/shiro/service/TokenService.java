package com.knight.shiro.service;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTDecodeException;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.interfaces.JWTVerifier;
import com.knight.config.ProjectConfigurationProperties;
import com.knight.entity.base.LoginUser;
import com.knight.entity.base.UserInfo;
import com.knight.entity.orm.SysUser;
import com.knight.service.ISysUserService;
import com.knight.utils.OauthUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Service;

import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import java.security.NoSuchAlgorithmException;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.time.temporal.ChronoUnit;
import java.util.Base64;
import java.util.Objects;

/**
 * 令牌服务
 *
 * @author lixiao
 * @since 2020/4/15 11:45
 */
@Service
@RequiredArgsConstructor
public class TokenService {

	/**
	 * 用户服务
	 */
	private final ISysUserService userService;

	/**
	 * 权限服务
	 */
	private final PermissionsService permissionsService;

	/**
	 * 项目属性
	 */
	private final ProjectConfigurationProperties projectProperties;

	/**
	 * hmac秘钥生成
	 * @return {@link String}
	 * @throws NoSuchAlgorithmException 未找到算法异常
	 */
	private static String hmacSecretGenerate() throws NoSuchAlgorithmException {
		KeyGenerator keyGenerator = KeyGenerator.getInstance("HmacSHA256");
		// 密钥长度为 256 位
		keyGenerator.init(256);
		SecretKey key = keyGenerator.generateKey();
		byte[] keyBytes = key.getEncoded();
		return Base64.getEncoder().encodeToString(keyBytes);
	}

	/**
	 * 获取当前登录的User对象
	 * @return User
	 */
	public LoginUser getLoginUser() {
		// 获取当前登录用户
		SysUser user = userService.selectUserBySubjectName(getSubject(getAccessToken()));
		if (Objects.isNull(user)) {
			return null;
		}
		LoginUser loginUser = new LoginUser();
		loginUser.setUser(new UserInfo(user));
		// 填充角色与权限
		permissionsService.addRoleAndPerms(loginUser);
		return loginUser;
	}

	/**
	 * 获取当前登录用户的token,如果token为null则获取refreshToken
	 * @return token
	 */
	public String getAccessToken() {
		return OauthUtils.getToken();
	}

	/**
	 * 创建访问令牌
	 * @param subject 当前用户唯一标识
	 * @return {@link String}
	 */
	public String createAccessToken(String subject) {
		ProjectConfigurationProperties.JwtProperties jwtProperties = projectProperties.getJwt();
		Algorithm algorithm = Algorithm.HMAC256(jwtProperties.getHmacSecret());
		// 令牌发行时间
		Instant iat = LocalDateTime.now().toInstant(ZoneOffset.ofHours(8));
		// 过期时间
		Instant exp = iat.plus(jwtProperties.getAccessTokenTimeToLive(), ChronoUnit.MINUTES);
		return JWT.create()
			.withIssuer(jwtProperties.getIssuer())
			.withSubject(subject)
			.withIssuedAt(iat)
			.withExpiresAt(exp)
			.sign(algorithm);
	}

	/**
	 * 创建刷新令牌
	 * @param subject 当前用户唯一标识
	 * @return {@link String}
	 */
	public String createRefreshToken(String subject) {
		ProjectConfigurationProperties.JwtProperties jwtProperties = projectProperties.getJwt();
		Algorithm algorithm = Algorithm.HMAC256(jwtProperties.getHmacSecret());
		// 令牌发行时间
		Instant iat = LocalDateTime.now().toInstant(ZoneOffset.ofHours(8));
		// 过期时间
		Instant exp = iat.plus(jwtProperties.getRefreshTokenTimeToLive(), ChronoUnit.MINUTES);
		return JWT.create()
			.withIssuer(jwtProperties.getIssuer())
			.withSubject(subject)
			.withIssuedAt(iat)
			.withExpiresAt(exp)
			.sign(algorithm);
	}

	/**
	 * 获取令牌中的Subject
	 * @param token 令牌
	 * @return {@link String}
	 */
	@Nullable
	public String getSubject(String token) {
		try {
			return JWT.decode(token).getSubject();
		}
		catch (JWTDecodeException e) {
			return null;
		}
	}

	/**
	 * 令牌验证
	 * @param token 令牌
	 * @return 是否有效
	 */
	public boolean verify(String token) {
		try {
			Algorithm algorithm = Algorithm.HMAC256(projectProperties.getJwt().getHmacSecret());
			JWTVerifier verifier = JWT.require(algorithm).build();
			verifier.verify(token);
			return true;
		}
		catch (JWTVerificationException exception) {
			return false;
		}
	}

}
