package com.knight.storage.template;

import com.baomidou.mybatisplus.core.toolkit.IdWorker;
import com.knight.entity.base.R;
import com.knight.storage.client.OssClient;
import com.knight.storage.enums.ObjectSignExpiryEnums;
import com.knight.storage.properties.OssProperties;
import com.knight.storage.vo.response.OssUploadR;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Paths;

/**
 * 存储模板
 *
 * @author knight
 * @since 2023/01/15
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class StorageTemplate implements InitializingBean {

	/**
	 * oss客户端
	 */
	private final OssClient ossClient;

	/**
	 * oss属性
	 */
	private final OssProperties ossProperties;

	/**
	 * 上传
	 * @param multipartFile multipartFile
	 * @return {@link R}<{@link Object}>
	 */
	public R<OssUploadR> upload(MultipartFile multipartFile) {
		return upload(Paths.get(IdWorker.getIdStr(), multipartFile.getOriginalFilename()).toString(), multipartFile);
	}

	/**
	 * 上传
	 * @param objectName 对象名称
	 * @param multipartFile multipartFile
	 * @return {@link R}<{@link Object}>
	 */
	public R<OssUploadR> upload(String objectName, MultipartFile multipartFile) {
		try (InputStream inputStream = multipartFile.getInputStream()) {
			return upload(objectName, inputStream);
		}
		catch (IOException e) {
			log.error("StorageTemplate#upload() {}", e.getLocalizedMessage());
			return R.failed();
		}
	}

	/**
	 * 上传
	 * @param objectName 对象名称
	 * @param inputStream 输入流
	 * @return {@link R}<{@link Object}>
	 */
	public R<OssUploadR> upload(String objectName, InputStream inputStream) {
		return upload(ossProperties.getDefaultBucket(), objectName, inputStream);
	}

	/**
	 * 上传
	 * @param objectName 对象名称
	 * @param inputStream 输入流
	 * @return {@link R}<{@link Object}>
	 */
	public R<OssUploadR> upload(String bucketName, String objectName, InputStream inputStream) {
		return R.ok(ossClient.upload(bucketName, objectName, inputStream));
	}

	/**
	 * 获取下载链接(默认十分钟有效时间)
	 * @param objectName 对象名称
	 * @return {@link R}<{@link String}>
	 */
	public R<String> getUrl(String objectName) {
		return getUrl(objectName, ObjectSignExpiryEnums.TEN_MINUTE);
	}

	/**
	 * 获取下载链接
	 * @param objectName 对象名称
	 * @param objectSignExpiryEnums 对象签名到期枚举
	 * @return {@link R}<{@link String}>
	 */
	public R<String> getUrl(String objectName, ObjectSignExpiryEnums objectSignExpiryEnums) {
		return getUrl(ossProperties.getDefaultBucket(), objectName, objectSignExpiryEnums);
	}

	/**
	 * 获取下载链接
	 * @param bucketName bucket名称
	 * @param objectName 对象名称
	 * @param objectSignExpiryEnums 对象签名到期枚举
	 * @return {@link R}<{@link String}>
	 */
	public R<String> getUrl(String bucketName, String objectName, ObjectSignExpiryEnums objectSignExpiryEnums) {
		return R.ok(ossClient.getUrl(bucketName, objectName, objectSignExpiryEnums.expirySeconds()));
	}

	/**
	 * 删除
	 * @param objectName 对象名称
	 * @return {@link R}<{@link Object}>
	 */
	public R<Object> remove(String objectName) {
		return remove(ossProperties.getDefaultBucket(), objectName);
	}

	/**
	 * 删除
	 * @param bucketName 桶名称
	 * @param objectName 对象名称
	 * @return {@link R}<{@link Object}>
	 */
	public R<Object> remove(String bucketName, String objectName) {
		ossClient.remove(bucketName, objectName);
		return R.ok();
	}

	@Override
	public void afterPropertiesSet() throws Exception {
		ossClient.init(ossProperties);
	}

}
