package com.knight.controller;

import com.knight.api.risk.api.ApiRisk;
import com.knight.entity.base.R;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.shiro.authz.annotation.RequiresUser;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Api(tags = "api风险认证测试端点")
@RequestMapping("/api/risk/test")
@RestController
public class ApiRiskTestController {

	@RequiresUser
	@GetMapping("/password")
	@ApiOperation(value = "测试密码认证")
	@ApiRisk
	public R<String> passwordVerify() {
		return R.ok();
	}

}
