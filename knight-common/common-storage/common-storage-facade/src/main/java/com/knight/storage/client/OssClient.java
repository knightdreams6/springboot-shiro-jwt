package com.knight.storage.client;

import com.knight.storage.enums.OssPlatformTypeEnums;
import com.knight.storage.properties.OssProperties;
import com.knight.storage.vo.response.OssUploadR;

import java.io.InputStream;

/**
 * oss客户端
 *
 * @author knight
 * @since 2023/01/15
 */
public interface OssClient {

	/**
	 * 平台
	 * @return {@link OssPlatformTypeEnums}
	 */
	OssPlatformTypeEnums platform();

	/**
	 * 初始化
	 */
	void init(OssProperties ossProperties);

	/**
	 * 上传
	 * @param bucketName 桶名称
	 * @param objectName 对象名称
	 * @param inputStream 输入流
	 * @return boolean
	 */
	OssUploadR upload(String bucketName, String objectName, InputStream inputStream);

	/**
	 * 获取下载链接
	 * @param bucketName bucket名称
	 * @param objectName 对象名称
	 * @param expireSeconds 过期时间 单位：秒
	 * @return {@link String}
	 */
	String getUrl(String bucketName, String objectName, Integer expireSeconds);

	/**
	 * 删除
	 * @param bucketName bucket名称
	 * @param objectName 对象名称
	 */
	void remove(String bucketName, String objectName);

}
