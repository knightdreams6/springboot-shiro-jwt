package com.knight.storage.template;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.toolkit.IdWorker;
import com.baomidou.mybatisplus.core.toolkit.StringPool;
import com.knight.entity.base.R;
import com.knight.storage.client.OssClient;
import com.knight.storage.enums.ObjectSignExpiryEnums;
import com.knight.storage.properties.OssProperties;
import com.knight.storage.response.StorageResultConstants;
import com.knight.storage.service.MultipartUploadService;
import com.knight.storage.vo.response.CreateMultipartUploadVo;
import com.knight.storage.vo.response.OssUploadR;
import com.knight.storage.vo.response.UploadPartVo;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;

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
	 * 分片上传服务
	 */
	private final MultipartUploadService multipartUploadService;

	/**
	 * 对象名称生成
	 * @param originalFilename 原始文件名
	 * @return {@link String}
	 */
	private String objectNameGenerate(String originalFilename) {
		return IdWorker.getIdStr() + StringPool.SLASH + originalFilename;
	}

	/**
	 * 上传
	 * @param multipartFile multipartFile
	 * @return {@link R}<{@link Object}>
	 */
	public R<OssUploadR> upload(MultipartFile multipartFile) {
		return upload(objectNameGenerate(multipartFile.getOriginalFilename()), multipartFile);
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

	/**
	 * 启动分片上传
	 * @param originFileName 原始文件名称
	 * @param fileHash 文件hash
	 * @param chunks 分片数量
	 * @param size 文件大小
	 * @return {@link R}<{@link CreateMultipartUploadVo}>
	 */
	public R<CreateMultipartUploadVo> initiateMultipartUpload(String originFileName, String fileHash, Integer chunks,
			Integer size) {
		CreateMultipartUploadVo vo = new CreateMultipartUploadVo();
		String uploadId = multipartUploadService.selectUploadIdByFileHash(fileHash);
		String bucketName = ossProperties.getDefaultBucket();
		if (StrUtil.isNotEmpty(uploadId)) {
			vo.setUploadId(uploadId);
			List<UploadPartVo> uploadPartVos = multipartUploadService.selectPartList(uploadId);
			vo.setParts(uploadPartVos);
		}
		else {
			String objectName = objectNameGenerate(originFileName);

			uploadId = ossClient.initiateMultipartUpload(bucketName, objectName);
			vo.setUploadId(uploadId);
			// 保存文件
			boolean saveFlag = multipartUploadService.saveFile(bucketName, objectName, fileHash, uploadId, chunks,
					size);
			if (!saveFlag) {
				return R.failed(StorageResultConstants.MULTIPART_START_FAIL);
			}
		}
		return R.ok(vo);
	}

	/**
	 * 上传分片
	 * @param uploadId 上传id
	 * @param partNumber 分片号
	 * @param multipartFile 分片文件
	 * @return {@link R}<{@link UploadPartVo}>
	 */
	public R<UploadPartVo> uploadPart(String uploadId, Integer partNumber, MultipartFile multipartFile) {
		String objectName = multipartUploadService.selectObjectNameByUploadId(uploadId);
		if (StrUtil.isEmpty(objectName)) {
			return R.failed();
		}

		String bucketName = ossProperties.getDefaultBucket();
		try (InputStream inputStream = multipartFile.getInputStream()) {
			int partSize = inputStream.available();
			UploadPartVo vo = ossClient.uploadPart(bucketName, objectName, uploadId, partNumber, inputStream, partSize);
			boolean saveFlag = multipartUploadService.savePart(bucketName, objectName, uploadId, vo.getPartNumber(),
					partSize, vo.getEtag());
			if (!saveFlag) {
				return R.failed(StorageResultConstants.PART_INFO_SAVE_FAIL);
			}
			return R.ok(vo);
		}
		catch (IOException e) {
			log.error("StorageTemplate#upload() {}", e.getLocalizedMessage());
			return R.failed();
		}
	}

	/**
	 * 完成分片上传
	 * @param uploadId 上传id
	 */
	public R<Object> completeMultipartUpload(String uploadId) {
		String objectName = multipartUploadService.selectObjectNameByUploadId(uploadId);
		if (StrUtil.isEmpty(objectName)) {
			return R.failed();
		}

		List<UploadPartVo> uploadPartVos = multipartUploadService.selectPartList(uploadId);
		if (CollUtil.isEmpty(uploadPartVos)) {
			return R.failed(StorageResultConstants.MULTIPART_EMPTY);
		}
		ossClient.completeMultipartUpload(ossProperties.getDefaultBucket(), objectName, uploadId, uploadPartVos);

		boolean completeFlag = multipartUploadService.complete(uploadId);
		return R.bool(completeFlag);
	}

	@Override
	public void afterPropertiesSet() throws Exception {
		ossClient.init(ossProperties);
	}

}
