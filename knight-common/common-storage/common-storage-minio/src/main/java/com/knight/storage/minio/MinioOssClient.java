package com.knight.storage.minio;

import com.knight.storage.client.OssClient;
import com.knight.storage.properties.OssProperties;
import com.knight.storage.vo.response.OssUploadR;
import io.minio.BucketExistsArgs;
import io.minio.GetPresignedObjectUrlArgs;
import io.minio.MakeBucketArgs;
import io.minio.MinioClient;
import io.minio.ObjectWriteResponse;
import io.minio.PutObjectArgs;
import io.minio.RemoveObjectArgs;
import io.minio.http.Method;
import lombok.SneakyThrows;
import org.springframework.stereotype.Component;

import java.io.InputStream;

/**
 * minio oss客户端
 *
 * @author knight
 * @since 2023/01/15
 */
@Component
public class MinioOssClient implements OssClient {

    /**
     * minio客户端
     */
    private MinioClient minioClient;

    @Override
    public void init(OssProperties ossProperties) {
        minioClient = MinioClient.builder()
                .endpoint(ossProperties.getEndpoint())
                .credentials(ossProperties.getAccessKey(), ossProperties.getSecretKey())
                .build();
        // 如果默认桶不存在则创建
        createBucketIfNotExist(ossProperties.getDefaultBucket());
    }

    /**
     * 创建桶(如果不存在)
     *
     * @param bucketName bucket名称
     */
    @SneakyThrows
    private void createBucketIfNotExist(String bucketName) {
        if (!minioClient.bucketExists(BucketExistsArgs.builder()
                .bucket(bucketName).build())) {
            minioClient.makeBucket(MakeBucketArgs.builder()
                    .bucket(bucketName)
                    .build());
        }
    }

    @SneakyThrows
    @Override
    public OssUploadR upload(String bucketName, String objectName, InputStream inputStream) {
        createBucketIfNotExist(bucketName);
        ObjectWriteResponse objectWriteResponse = minioClient.putObject(PutObjectArgs.builder()
                .bucket(bucketName).object(objectName)
                .stream(inputStream, inputStream.available(), -1)
                .build());
        OssUploadR ossUploadR = new OssUploadR();
        ossUploadR.setBucket(objectWriteResponse.bucket());
        ossUploadR.setObjectName(objectWriteResponse.object());
        ossUploadR.setEtag(objectWriteResponse.etag());
        ossUploadR.setVersionId(objectWriteResponse.versionId());
        return ossUploadR;
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
        minioClient.removeObject(RemoveObjectArgs.builder()
                .bucket(bucketName)
                .object(objectName)
                .build());
    }

}
