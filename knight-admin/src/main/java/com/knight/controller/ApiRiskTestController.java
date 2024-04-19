package com.knight.controller;

import com.knight.api.risk.api.ApiRisk;
import com.knight.entity.base.R;
import org.apache.shiro.authz.annotation.RequiresUser;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * api风险认证测试端点
 *
 * @author knight
 */
@RequestMapping("/api/risk/test")
@RestController
public class ApiRiskTestController {

	/**
	 * 测试密码认证
	 * @return R<Void>
	 */
	@RequiresUser
	@GetMapping("/password")
	@ApiRisk
	public R<Void> passwordVerify() {
		return R.ok();
	}

}
