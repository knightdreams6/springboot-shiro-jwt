package com.knight.api.risk.endpoint;

import com.knight.api.constants.ApiResultConstants;
import com.knight.api.risk.api.dto.ApiRiskInfoDto;
import com.knight.api.risk.component.ApiRiskUserService;
import com.knight.api.risk.component.ApiRiskVerificationCache;
import com.knight.entity.base.R;
import lombok.RequiredArgsConstructor;
import org.apache.shiro.authz.annotation.RequiresUser;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * api风险认证端点
 *
 * @author knight
 * @since 2023/12/14
 */
@RequestMapping("/api/risk/verify")
@RestController
@RequiredArgsConstructor
public class ApiRiskVerificationEndpoint {

	/**
	 * 密码服务
	 */
	private final ApiRiskUserService apiRiskUserService;

	/**
	 * api风险验证缓存
	 */
	private final ApiRiskVerificationCache apiRiskVerificationCache;

	/**
	 * 密码验证
	 * @param code code
	 * @param password 密码
	 * @return R<Void>
	 */
	@RequiresUser
	@PostMapping(params = "type=PASSWORD")
	public R<Void> passwordVerify(@RequestParam String code, @RequestParam String password) {
		ApiRiskInfoDto riskInfo = apiRiskVerificationCache.getRiskInfo(code);
		if (riskInfo == null) {
			return R.failed(ApiResultConstants.API_RISK_INFO_NOT_FOUND);
		}

		// 查询当前用户密码
		boolean flag = apiRiskUserService.passwordsMatch(riskInfo.getPrincipal(), password);
		if (flag) {
			apiRiskVerificationCache.save(riskInfo.getPrincipal(), riskInfo.getApiPath(), riskInfo.getExpired(),
					riskInfo.getExpiredUnit());
		}
		return R.bool(flag);
	}

	@RequiresUser
	@PostMapping(params = "type=MAIL_CODE")
	public R<Void> mailCodeVerify() {
		return R.ok();
	}

}
