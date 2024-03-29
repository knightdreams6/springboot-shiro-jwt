package com.knight.storage.properties;

import com.knight.storage.enums.OssPlatformTypeEnums;
import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * oss属性
 *
 * @author knight
 * @since 2023/01/15
 */
@Data
@Component
@ConfigurationProperties(prefix = "storage")
public class OssProperties {

	/**
	 * oss平台类型
	 */
	private OssPlatformTypeEnums platformType;

	/**
	 * 端点
	 */
	private String endpoint;

	/**
	 * 访问密钥
	 */
	private String accessKey;

	/**
	 * 秘密密钥
	 */
	private String secretKey;

	/**
	 * 默认桶
	 */
	private String defaultBucket;

}
