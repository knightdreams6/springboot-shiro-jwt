package com.knight.shiro.filter;

import cn.hutool.json.JSONUtil;
import com.knight.entity.base.Result;
import com.knight.entity.enums.ErrorState;
import com.knight.utils.ServletUtils;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.BearerToken;
import org.apache.shiro.web.filter.authc.BearerHttpAuthenticationFilter;

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletResponse;

/**
 * oauth过滤器
 *
 * @author knight
 * @date 2022/12/14
 */
public class OauthFilter extends BearerHttpAuthenticationFilter {

	@Override
	protected boolean onLoginFailure(AuthenticationToken token, AuthenticationException e, ServletRequest request,
			ServletResponse response) {
		// 处理登录失败异常
		if (token instanceof BearerToken) {
			ServletUtils.renderString((HttpServletResponse) response,
					JSONUtil.toJsonStr(Result.error(ErrorState.ACCESS_TOKEN_INVALID)));
		}
		return super.onLoginFailure(token, e, request, response);
	}

}
