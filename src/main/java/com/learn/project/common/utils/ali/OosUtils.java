package com.learn.project.common.utils.ali;

import com.aliyun.oss.ClientException;
import com.aliyun.oss.OSS;
import com.aliyun.oss.OSSClientBuilder;
import com.aliyun.oss.OSSException;
import com.aliyun.oss.model.Bucket;
import com.aliyun.oss.model.CannedAccessControlList;
import com.aliyun.oss.model.CreateBucketRequest;
import com.aliyun.oss.model.ListBucketsRequest;
import com.aliyun.oss.model.ObjectListing;
import com.aliyun.oss.model.ObjectMetadata;
import com.aliyun.oss.model.PutObjectRequest;
import com.learn.project.common.utils.FileUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.util.List;

/**
 * @author lixiao
 * @version 1.0
 * @date 2020/4/15 13:09
 */
@Slf4j
public class OosUtils {

    /**
     * 访问OSS的域名
     */
    private static final String endpoint = "https://oss-cn-beijing.aliyuncs.com";
    private static final String accessKeyId = "";
    private static final String accessKeySecret = "";
    private static final String bucketName = "";


    /**
     * 创建bucket
     */
    public static void createBucket(OSS ossClient, String bucketName) {
        ossClient.createBucket(bucketName);
        CreateBucketRequest createBucketRequest = new CreateBucketRequest(bucketName);
        createBucketRequest.setCannedACL(CannedAccessControlList.PublicRead);
        ossClient.createBucket(createBucketRequest);
    }

    /**
     * List the buckets in your account
     */
    public static List<Bucket> listBucket(OSS ossClient) {
        ListBucketsRequest listBucketsRequest = new ListBucketsRequest();
        listBucketsRequest.setMaxKeys(500);
        return ossClient.listBuckets();
    }

    /**
     * List objects in your bucket by prefix
     * for (OSSObjectSummary objectSummary : objectListing.getObjectSummaries()) {
     * System.out.println(" - " + objectSummary.getKey() + "  " +
     * "(size = " + objectSummary.getSize() + ")");
     * }
     */
    public static ObjectListing listObjects(OSS ossClient) {
        return ossClient.listObjects(bucketName);
    }


    /**
     * 删除当前bucket中的文件
     *
     * @param path 文件路径
     */
    public static void deletedFile(String path) {
        OSS ossClient = new OSSClientBuilder().build(endpoint, accessKeyId, accessKeySecret);
        ossClient.deleteObject(bucketName, path);
    }


    /**
     * 上传文件到阿里oos
     *
     * @param file 文件
     * @param path 文件上传路径
     * @return 上传路径
     */
    public static String upload(MultipartFile file, String path) {
        OSS ossClient = new OSSClientBuilder().build(endpoint, accessKeyId, accessKeySecret);
        try {
            if (!ossClient.doesBucketExist(bucketName)) {
                createBucket(ossClient, bucketName);
            }
            String imageName = FileUtils.getFileName(file.getOriginalFilename());
            // file
            File transfer = FileUtils.transfer(file);
            // metadata
            ObjectMetadata metadata = new ObjectMetadata();
            metadata.setContentType(file.getContentType());
            ossClient.putObject(new PutObjectRequest(bucketName, path + imageName, transfer, metadata));
            return path + imageName;
        } catch (OSSException oe) {
            log.error("Caught an OSSException, which means your request made it to OSS, "
                    + "but was rejected with an error response for some reason.");
            log.error("Error Message: " + oe.getErrorMessage());
            log.error("Error Code:       " + oe.getErrorCode());
            log.error("Request ID:      " + oe.getRequestId());
            log.error("Host ID:           " + oe.getHostId());
        } catch (ClientException ce) {
            log.error("Caught an ClientException, which means the client encountered "
                    + "a serious internal problem while trying to communicate with OSS, "
                    + "such as not being able to access the network.");
            log.error("Error Message: " + ce.getMessage());
        } finally {
            /*
             * Do not forget to shut down the client finally to release all allocated resources.
             */
            ossClient.shutdown();
        }
        return null;
    }

    /**
     * 更新文件
     *
     * @param file       要更新的文件
     * @param uploadPath 更新的文件上传路径
     * @param originPath 原始文件路径
     * @return 新文件上传路径
     */
    public static String updateFile(MultipartFile file, String uploadPath, String originPath) {
        if (file != null && !file.isEmpty()) {
            String imageName = upload(file, uploadPath);
            if (!StringUtils.isEmpty(originPath)) {
                deletedFile(originPath);
            }
            return imageName;
        }
        return null;
    }
}
