package com.learn.project.common.utils;

import com.aliyun.oss.ClientException;
import com.aliyun.oss.OSS;
import com.aliyun.oss.OSSClientBuilder;
import com.aliyun.oss.OSSException;
import com.aliyun.oss.model.*;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.util.List;

/**
 * @author lixiao
 * @version 1.0
 * @date 2020/4/15 13:09
 */

public class OosUtils {

    /**
     * 访问OSS的域名
     */
    private static String endpoint = "https://oss-cn-beijing.aliyuncs.com";
    private static String accessKeyId = "";
    private static String accessKeySecret = "";
    private static String bucketName = "";
    /**
     * 文件存储基础目录
     */
    private static String basePath = "backstage";

    /**
     * 创建bucket
     */
    public static void createBucket(OSS ossClient, String bucketName){
        ossClient.createBucket(bucketName);
        CreateBucketRequest createBucketRequest= new CreateBucketRequest(bucketName);
        createBucketRequest.setCannedACL(CannedAccessControlList.PublicRead);
        ossClient.createBucket(createBucketRequest);
    }

    /**
     * List the buckets in your account
     */
    public static List<Bucket> listBucket(OSS ossClient){
        ListBucketsRequest listBucketsRequest = new ListBucketsRequest();
        listBucketsRequest.setMaxKeys(500);
        return ossClient.listBuckets();
    }

    /**
     * List objects in your bucket by prefix
     */
    public static ObjectListing listObjects(OSS ossClient){
        ObjectListing objectListing = ossClient.listObjects(bucketName);
        for (OSSObjectSummary objectSummary : objectListing.getObjectSummaries()) {
            System.out.println(" - " + objectSummary.getKey() + "  " +
                    "(size = " + objectSummary.getSize() + ")");
        }
        return objectListing;
    }


    /**
     * 删除当前bucket中的文件
     * @param path 文件路径
     */
    public static void deletedFile(String path){
        OSS ossClient = new OSSClientBuilder().build(endpoint, accessKeyId, accessKeySecret);
        ossClient.deleteObject(bucketName, basePath + path);
    }


    /**
     * 上传文件到阿里oos
     * @param file 文件
     * @param path 文件上传路径
     * @return 上传路径
     */
    public static String upload(MultipartFile file, String path){
        OSS ossClient = new OSSClientBuilder().build(endpoint, accessKeyId, accessKeySecret);
        try{
            if (!ossClient.doesBucketExist(bucketName)) {
                createBucket(ossClient, bucketName);
            }
            String imageName = FileUtils.getFileName(file.getOriginalFilename());
            // file
            File transfer = FileUtils.transfer(file);
            // metadata
            ObjectMetadata metadata = new ObjectMetadata();
            metadata.setContentType(file.getContentType());
            ossClient.putObject(new PutObjectRequest(bucketName, path + imageName, transfer , metadata));
            return imageName;
        }  catch (OSSException oe) {
            System.out.println("Caught an OSSException, which means your request made it to OSS, "
                    + "but was rejected with an error response for some reason.");
            System.out.println("Error Message: " + oe.getErrorMessage());
            System.out.println("Error Code:       " + oe.getErrorCode());
            System.out.println("Request ID:      " + oe.getRequestId());
            System.out.println("Host ID:           " + oe.getHostId());
        } catch (ClientException ce) {
            System.out.println("Caught an ClientException, which means the client encountered "
                    + "a serious internal problem while trying to communicate with OSS, "
                    + "such as not being able to access the network.");
            System.out.println("Error Message: " + ce.getMessage());
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
     * @param file 要更新的文件
     * @param uploadPath 更新的文件上传路径
     * @param originPath 原始文件路径
     * @return 新文件上传路径
     */
    public static String updateFile(MultipartFile file, String uploadPath, String originPath){
        if(file != null && !file.isEmpty()){
            String imageName = upload(file, uploadPath);
            if(!StringUtils.isEmpty(originPath)){
                deletedFile(basePath + originPath);
            }
            return imageName;
        }
        return null;
    }
}
