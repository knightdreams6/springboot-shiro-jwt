package com.knight.storage.minio;

import com.knight.storage.properties.OssProperties;
import io.minio.CreateMultipartUploadResponse;
import io.minio.MinioAsyncClient;
import io.minio.UploadPartResponse;
import io.minio.errors.InsufficientDataException;
import io.minio.errors.InternalException;
import io.minio.errors.XmlParserException;
import io.minio.messages.Part;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.io.InputStream;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.concurrent.ExecutionException;

/**
 * minio oss客户端
 *
 * @author knight
 * @since 2023/01/15
 */
@Component
public class MinioOssMultipartClient extends MinioAsyncClient {

	public MinioOssMultipartClient(OssProperties ossProperties) {
		super(MinioAsyncClient.builder()
			.endpoint(ossProperties.getEndpoint())
			.credentials(ossProperties.getAccessKey(), ossProperties.getSecretKey())
			.build());
	}

	/**
	 * 启动分片上传
	 * @param bucketName bucket名称
	 * @param objectName 对象名称
	 * @return {@link CreateMultipartUploadResponse}
	 */
	public CreateMultipartUploadResponse initiateMultipartUpload(String bucketName, String objectName)
			throws InsufficientDataException, IOException, NoSuchAlgorithmException, InvalidKeyException,
			XmlParserException, InternalException, ExecutionException, InterruptedException {
		return super.createMultipartUploadAsync(bucketName, null, objectName, null, null).get();
	}

	/**
	 * 上传分片
	 * @param bucketName bucket名称
	 * @param objectName 对象名称
	 * @param uploadId 上传id
	 * @param partNumber 分片号
	 * @param partStream 分片流
	 * @param partSize 分片大小
	 * @return {@link UploadPartResponse}
	 */
	public UploadPartResponse uploadPart(String bucketName, String objectName, String uploadId, Integer partNumber,
			InputStream partStream, long partSize)
			throws IOException, InsufficientDataException, NoSuchAlgorithmException, InvalidKeyException,
			XmlParserException, InternalException, ExecutionException, InterruptedException {
		return super.uploadPartAsync(bucketName, null, objectName, partStream, partSize, uploadId, partNumber, null,
				null)
			.get();
	}

	/**
	 * 完成分片上传
	 * @param bucketName bucket名称
	 * @param objectName 对象名称
	 * @param uploadId 上传id
	 * @param partArray 分片数组
	 */
	public void completeMultipartUpload(String bucketName, String objectName, String uploadId, Part[] partArray)
			throws InsufficientDataException, IOException, NoSuchAlgorithmException, InvalidKeyException,
			XmlParserException, InternalException {
		super.completeMultipartUploadAsync(bucketName, null, objectName, uploadId, partArray, null, null);
	}

}
