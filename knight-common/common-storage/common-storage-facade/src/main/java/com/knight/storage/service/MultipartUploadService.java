package com.knight.storage.service;

import com.knight.storage.vo.response.UploadPartVo;
import org.springframework.lang.Nullable;

import java.util.List;

/**
 * 多部分上传服务
 *
 * @author knight
 * @since 2023/06/29
 */
public interface MultipartUploadService {

	/**
	 * 根据文件hash判断文件是否已上传
	 * @param fileHash 文件哈希
	 * @return String 如果不为空则代表上传过改文件
	 */
	@Nullable
	String selectUploadIdByFileHash(String fileHash);

	/**
	 * 保存文件
	 * @param bucketName 桶名称
	 * @param objectName 对象名称
	 * @param fileHash 文件哈希
	 * @param uploadId 上传id
	 * @param chunks 块
	 * @param size 大小
	 * @return boolean
	 */
	boolean saveFile(String bucketName, String objectName, String fileHash, String uploadId, Integer chunks,
			Integer size);

	/**
	 * 保存分片信息
	 * @param bucketName 桶名称
	 * @param objectName 对象名称
	 * @param uploadId 上传身份证
	 * @param partNumber 分片号
	 * @param partSize 分片大小
	 * @param etag etag
	 * @return boolean
	 */
	boolean savePart(String bucketName, String objectName, String uploadId, Integer partNumber, Integer partSize,
			String etag);

	/**
	 * 查询分片列表
	 * @param uploadId 上传id
	 * @return {@link List}<{@link UploadPartVo}>
	 */
	List<UploadPartVo> selectPartList(String uploadId);

	/**
	 * 完成分片上传
	 * @param uploadId 上传Id
	 * @return boolean
	 */
	boolean complete(String uploadId);

	/**
	 * 根据上传id查询对象名称
	 * @param uploadId 上传身份证
	 * @return {@link String}
	 */
	@Nullable
	String selectObjectNameByUploadId(String uploadId);

}
