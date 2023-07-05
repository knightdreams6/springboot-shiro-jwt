package com.knight.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.NestedConfigurationProperty;
import org.springframework.stereotype.Component;

import java.util.concurrent.TimeUnit;

/**
 * 项目配置属性
 *
 * @author knight
 * @since 2023/07/05
 */
@Data
@Component
@ConfigurationProperties(prefix = "project")
public class ProjectConfigurationProperties {

	/**
	 * jwt属性
	 */
	@NestedConfigurationProperty
	private JwtProperties jwt;

	@Data
	public static class JwtProperties {

		/**
		 * 发行人
		 */
		private String issuer;

		/**
		 * hmac秘钥
		 */
		private String hmacSecret;

		/**
		 * 访问令牌时间生活 单位:分钟 默认30分钟
		 */
		private Long accessTokenTimeToLive = 30L;

		/**
		 * 刷新令牌时间存活时间 单位:分钟 默认1天
		 */
		private Long refreshTokenTimeToLive = TimeUnit.DAYS.toMinutes(1);

	}

}
