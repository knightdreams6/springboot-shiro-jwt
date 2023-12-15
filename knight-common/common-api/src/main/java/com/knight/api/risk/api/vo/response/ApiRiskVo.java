package com.knight.api.risk.api.vo.response;

import com.knight.api.risk.api.ApiRiskVerificationType;
import lombok.Data;

/**
 * api风险认证vo
 *
 * @author knight
 * @since 2023/12/14
 */
@Data
public class ApiRiskVo {

	private ApiRiskVerificationType type;

	private String code;

}
