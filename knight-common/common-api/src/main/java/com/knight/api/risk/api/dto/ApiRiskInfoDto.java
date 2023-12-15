package com.knight.api.risk.api.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.io.Serializable;
import java.util.concurrent.TimeUnit;

/**
 * api认证信息dto
 *
 * @author knight
 * @since 2023/12/14
 */
@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class ApiRiskInfoDto implements Serializable {

	/**
	 * 当前登录用户
	 */
	private String principal;

	/**
	 * api路径
	 */
	private String apiPath;

	/**
	 * 过期时间
	 */
	private Long expired;

	/**
	 * 过期时间单位
	 */
	private TimeUnit expiredUnit;

}
