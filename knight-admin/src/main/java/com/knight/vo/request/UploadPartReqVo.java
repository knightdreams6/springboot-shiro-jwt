package com.knight.vo.request;

import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

/**
 * 上传分片请求参数vo
 *
 * @author knight
 * @since 2023/06/30
 */
@Data
public class UploadPartReqVo {

	/**
	 * 上传id
	 */
	private String uploadId;

	/**
	 * 分片数
	 */
	private Integer partNumber;

	/**
	 * 分片文件
	 */
	private MultipartFile partFile;

}
