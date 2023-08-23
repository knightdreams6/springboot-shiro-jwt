package com.knight.storage.minio;

import cn.hutool.core.io.FileUtil;
import com.knight.storage.client.OssClient;
import com.knight.storage.enums.OssPlatformTypeEnums;
import com.knight.storage.properties.OssProperties;
import com.knight.storage.vo.response.OssUploadR;
import com.knight.storage.vo.response.UploadPartVo;
import io.minio.*;
import io.minio.http.Method;
import io.minio.messages.InitiateMultipartUploadResult;
import io.minio.messages.Part;
import lombok.SneakyThrows;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.stereotype.Component;

import java.io.InputStream;
import java.util.List;
import java.util.concurrent.CompletableFuture;

/**
 * minio oss客户端
 *
 * @author knight
 * @since 2023/01/15
 */
@Component
@ConditionalOnClass(S3Base.class)
public class MinioOssClient implements OssClient {

	/**
	 * minio客户端
	 */
	private MinioOssMultipartClient minioClient;

	@Override
	public OssPlatformTypeEnums platform() {
		return OssPlatformTypeEnums.MINIO;
	}

	@Override
	public void init(OssProperties ossProperties) {
		minioClient = new MinioOssMultipartClient(ossProperties);
		// 如果默认桶不存在则创建
		createBucketIfNotExist(ossProperties.getDefaultBucket());
	}

	/**
	 * 创建桶(如果不存在)
	 * @param bucketName bucket名称
	 */
	@SneakyThrows
	private void createBucketIfNotExist(String bucketName) {
		if (!minioClient.bucketExists(BucketExistsArgs.builder().bucket(bucketName).build()).get()) {
			minioClient.makeBucket(MakeBucketArgs.builder().bucket(bucketName).build()).get();
		}
	}

	@SneakyThrows
	@Override
	public OssUploadR upload(String bucketName, String objectName, InputStream inputStream) {
		createBucketIfNotExist(bucketName);
		ObjectWriteResponse objectWriteResponse = minioClient
			.putObject(PutObjectArgs.builder()
				.bucket(bucketName)
				.object(objectName)
				.stream(inputStream, inputStream.available(), -1)
				.build())
			.get();
		return OssUploadR.builder()
			.bucket(objectWriteResponse.bucket())
			.objectName(objectWriteResponse.object())
			.etag(objectWriteResponse.etag())
			.versionId(objectWriteResponse.versionId())
			.build();
	}

	@SneakyThrows
	@Override
	public String getUrl(String bucketName, String objectName, Integer expireSeconds) {
		return minioClient.getPresignedObjectUrl(GetPresignedObjectUrlArgs.builder()
			.bucket(bucketName)
			.object(objectName)
			.method(Method.GET)
			.expiry(expireSeconds)
			.build());
	}

	@SneakyThrows
	@Override
	public void remove(String bucketName, String objectName) {
		minioClient.removeObject(RemoveObjectArgs.builder().bucket(bucketName).object(objectName).build()).get();
	}

	@SneakyThrows
	@Override
	public void download(String bucketName, String objectName, String destPath) {
		CompletableFuture<GetObjectResponse> completableFuture = minioClient
			.getObject(GetObjectArgs.builder().bucket(bucketName).object(objectName).build());
		GetObjectResponse getObjectResponse = completableFuture.get();
		FileUtil.writeFromStream(getObjectResponse, destPath);
	}

	@SneakyThrows
	@Override
	public String initiateMultipartUpload(String bucketName, String objectName) {
		CreateMultipartUploadResponse createMultipartUploadResponse = minioClient.initiateMultipartUpload(bucketName,
				objectName);
		InitiateMultipartUploadResult result = createMultipartUploadResponse.result();
		return result.uploadId();
	}

	@SneakyThrows
	@Override
	public UploadPartVo uploadPart(String bucketName, String objectName, String uploadId, Integer partNumber,
			InputStream partStream, long partSize) {
		UploadPartResponse uploadPartResponse = minioClient.uploadPart(bucketName, objectName, uploadId, partNumber,
				partStream, partSize);
		return UploadPartVo.builder()
			.etag(uploadPartResponse.etag())
			.partNumber(uploadPartResponse.partNumber())
			.build();
	}

	@SneakyThrows
	@Override
	public void completeMultipartUpload(String bucketName, String objectName, String uploadId,
			List<UploadPartVo> partVos) {
		Part[] partArray = partVos.stream()
			.map(uploadPartVo -> new Part(uploadPartVo.getPartNumber(), uploadPartVo.getEtag()))
			.toArray(Part[]::new);
		minioClient.completeMultipartUpload(bucketName, objectName, uploadId, partArray);
	}

}
