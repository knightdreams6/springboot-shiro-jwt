package com.knight.storage.aliyun;

import com.aliyun.oss.HttpMethod;
import com.aliyun.oss.OSS;
import com.aliyun.oss.OSSClientBuilder;
import com.aliyun.oss.model.*;
import com.knight.storage.client.OssClient;
import com.knight.storage.enums.OssPlatformTypeEnums;
import com.knight.storage.properties.OssProperties;
import com.knight.storage.vo.response.OssUploadR;
import com.knight.storage.vo.response.UploadPartVo;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.stereotype.Component;

import java.io.InputStream;
import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

/**
 * 阿里云对象存储客户端
 *
 * @author knight
 * @since 2023/06/01
 */
@Component
@ConditionalOnClass(OSS.class)
public class AliYunOssClient implements OssClient {

	/**
	 * aliyun oss客户端
	 */
	private OSS ossClient;

	@Override
	public OssPlatformTypeEnums platform() {
		return OssPlatformTypeEnums.ALIYUN;
	}

	@Override
	public void init(OssProperties ossProperties) {
		ossClient = new OSSClientBuilder().build(ossProperties.getEndpoint(), ossProperties.getAccessKey(),
				ossProperties.getSecretKey());
	}

	@Override
	public OssUploadR upload(String bucketName, String objectName, InputStream inputStream) {
		// 构建上传对象请求参数
		PutObjectRequest putObjectRequest = new PutObjectRequest(bucketName, objectName, inputStream);
		// 上传对象返回值
		PutObjectResult putObjectResult = ossClient.putObject(putObjectRequest);
		return OssUploadR.builder()
			.etag(putObjectResult.getETag())
			.versionId(putObjectResult.getVersionId())
			.bucket(bucketName)
			.objectName(objectName)
			.build();
	}

	@Override
	public String getUrl(String bucketName, String objectName, Integer expireSeconds) {
		GeneratePresignedUrlRequest request = new GeneratePresignedUrlRequest(bucketName, objectName, HttpMethod.GET);
		request.setExpiration(new Date(System.currentTimeMillis() + TimeUnit.SECONDS.toMillis(expireSeconds)));
		return ossClient.generatePresignedUrl(request).toString();
	}

	@Override
	public void remove(String bucketName, String objectName) {
		ossClient.deleteObject(bucketName, objectName);
	}

	@Override
	public String initiateMultipartUpload(String bucketName, String objectName) {
		InitiateMultipartUploadResult initiateMultipartUploadResult = ossClient
			.initiateMultipartUpload(new InitiateMultipartUploadRequest(bucketName, objectName));
		return initiateMultipartUploadResult.getUploadId();
	}

	@Override
	public UploadPartVo uploadPart(String bucketName, String objectName, String uploadId, Integer partNumber,
			InputStream partStream, long partSize) {
		UploadPartRequest uploadPartRequest = new UploadPartRequest(bucketName, objectName, uploadId, partNumber,
				partStream, partSize);
		UploadPartResult uploadPartResult = ossClient.uploadPart(uploadPartRequest);
		return UploadPartVo.builder()
			.partNumber(uploadPartResult.getPartNumber())
			.etag(uploadPartResult.getETag())
			.build();
	}

	@Override
	public void completeMultipartUpload(String bucketName, String objectName, String uploadId,
			List<UploadPartVo> partVos) {
		List<PartETag> partETags = partVos.stream()
			.map(partVo -> new PartETag(partVo.getPartNumber(), partVo.getEtag()))
			.collect(Collectors.toList());
		ossClient
			.completeMultipartUpload(new CompleteMultipartUploadRequest(bucketName, objectName, uploadId, partETags));
	}

}
