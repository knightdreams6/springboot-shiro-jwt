package com.knight.api.risk.component;

import cn.hutool.core.util.StrUtil;
import cn.hutool.json.JSONUtil;
import com.knight.api.risk.api.dto.ApiRiskInfoDto;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;

import java.util.concurrent.TimeUnit;

/**
 * api风险认证缓存默认实现
 *
 * @author knight
 * @since 2023/12/14
 */
@Component
@RequiredArgsConstructor
public class ApiRiskVerificationCacheDefaultImpl implements ApiRiskVerificationCache {

	/**
	 * redis 操作模板
	 */
	private final StringRedisTemplate stringRedisTemplate;

	@Override
	public String saveRiskInfo(ApiRiskInfoDto apiRiskInfoDto) {
		String code = generateRiskInfoCode(apiRiskInfoDto.getPrincipal(), apiRiskInfoDto.getApiPath());

		String riskInfoKey = getRiskInfoKey(code);
		stringRedisTemplate.opsForValue().set(riskInfoKey, JSONUtil.toJsonStr(apiRiskInfoDto), 5, TimeUnit.MINUTES);
		return code;
	}

	@Override
	public ApiRiskInfoDto getRiskInfo(String code) {
		String riskInfoKey = getRiskInfoKey(code);
		String apiRiskInfoDtoJsonStr = stringRedisTemplate.opsForValue().get(riskInfoKey);
		if (StrUtil.isEmpty(apiRiskInfoDtoJsonStr)) {
			return null;
		}

		// 只允许获取一次
		stringRedisTemplate.delete(riskInfoKey);
		return JSONUtil.toBean(apiRiskInfoDtoJsonStr, ApiRiskInfoDto.class);
	}

	@Override
	public boolean hasKey(String principal, String apiPath) {
		return Boolean.TRUE.equals(stringRedisTemplate.hasKey(getKey(principal, apiPath)));
	}

	@Override
	public void save(String principal, String apiPath, Long expire, TimeUnit expireUnit) {
		stringRedisTemplate.opsForValue().set(getKey(principal, apiPath), "", expire, expireUnit);
	}

}
