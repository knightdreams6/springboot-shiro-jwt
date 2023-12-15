package com.knight.api.risk.component;

import cn.hutool.crypto.digest.MD5;
import com.knight.api.risk.api.dto.ApiRiskInfoDto;
import org.springframework.lang.Nullable;

import java.time.Instant;
import java.util.concurrent.TimeUnit;

/**
 * api风险认证缓存
 *
 * @author knight
 * @since 2023/12/14
 */
public interface ApiRiskVerificationCache {

	/**
	 * 获取当前缓存key
	 * @param principal 当前用户主体
	 * @param apiPath 当前接口地址
	 * @return String 当前缓存key
	 */
	default String getKey(String principal, String apiPath) {
		return "API:RISK:" + principal + ":" + apiPath;
	}

	/**
	 * 获取当前缓存信息key
	 * @param code 缓存信息code
	 * @return String 当前缓存key
	 */
	default String getRiskInfoKey(String code) {
		return "API:RISK:INFO:" + code;
	}

	/**
	 * 获取当前缓存信息key
	 * @param principal 当前用户主体
	 * @param apiPath 当前接口地址
	 * @return String 当前缓存key
	 */
	default String generateRiskInfoCode(String principal, String apiPath) {
		return MD5.create().digestHex(principal + apiPath + Instant.now().getEpochSecond());
	}

	/**
	 * 保存风险认证信息
	 * @param apiRiskInfoDto 风险认证信息
	 * @return {@link String} 风险认证信息code
	 */
	String saveRiskInfo(ApiRiskInfoDto apiRiskInfoDto);

	/**
	 * 获取风险认证信息
	 * @param code 风险认证信息code
	 * @return {@link ApiRiskInfoDto}
	 */
	@Nullable
	ApiRiskInfoDto getRiskInfo(String code);

	/**
	 * 判断是否存在认证信息
	 * @param principal 当前用户主体
	 * @param apiPath 当前接口地址
	 * @return boolean
	 */
	boolean hasKey(String principal, String apiPath);

	/**
	 * 风险认证
	 * @param principal 当前用户主体
	 * @param apiPath 当前接口地址
	 * @param expire 缓存过期时间
	 * @param expireUnit 缓存过期时间单位
	 */
	void save(String principal, String apiPath, Long expire, TimeUnit expireUnit);

}
