package com.knight.api.risk.component;

import cn.hutool.extra.servlet.ServletUtil;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.knight.api.constants.ApiResultConstants;
import com.knight.api.risk.api.ApiRisk;
import com.knight.api.risk.api.dto.ApiRiskInfoDto;
import com.knight.api.risk.api.vo.response.ApiRiskVo;
import com.knight.entity.base.R;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.SecurityUtils;
import org.springframework.http.MediaType;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Component;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * api风险认证接口
 *
 * @author knight
 * @since 2023/12/14
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class ApiRiskInterceptor implements HandlerInterceptor {

	/**
	 * api风险认证缓存
	 */
	private final ApiRiskVerificationCache apiRiskVerificationCache;

	/**
	 * json序列化mapper
	 */
	private final ObjectMapper objectMapper;

	@Override
	public boolean preHandle(@NonNull HttpServletRequest request, @NonNull HttpServletResponse response,
			@NonNull Object handler) throws Exception {
		if (!(handler instanceof HandlerMethod)) {
			return true;
		}
		HandlerMethod handlerMethod = (HandlerMethod) handler;
		// 获取ApiRisk注解
		ApiRisk apiRisk = handlerMethod.getMethodAnnotation(ApiRisk.class);
		if (apiRisk == null) {
			return true;
		}

		// 当前登录用户
		Object principal = SecurityUtils.getSubject().getPrincipal();
		if (principal == null) {
			return true;
		}

		// 当前接口路径
		String apiPath = request.getMethod() + request.getServletPath();

		// 判断是否已经认证
		boolean verified = apiRiskVerificationCache.hasKey(principal.toString(), apiPath);
		if (verified) {
			return true;
		}

		log.debug("接口风险认证限制拦截器执行了...");

		// 缓存当前认证信息
		String code = apiRiskVerificationCache
			.saveRiskInfo(new ApiRiskInfoDto(principal.toString(), apiPath, apiRisk.expired(), apiRisk.expiredUnit()));

		// 返回需要认证 并返回认证类型
		R<Object> failedR = R.failed(ApiResultConstants.API_RISK);
		ApiRiskVo vo = new ApiRiskVo();
		vo.setType(apiRisk.verifyType());
		vo.setCode(code);
		failedR.setData(vo);
		ServletUtil.write(response, objectMapper.writeValueAsString(failedR), MediaType.APPLICATION_JSON_UTF8_VALUE);
		return false;
	}

}
