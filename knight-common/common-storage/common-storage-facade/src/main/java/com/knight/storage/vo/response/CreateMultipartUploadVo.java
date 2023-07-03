package com.knight.storage.vo.response;

import lombok.Data;

import java.util.List;

/**
 * 创建分片上传返回值vo
 *
 * @author knight
 * @since 2023/06/29
 */
@Data
public class CreateMultipartUploadVo {

	/**
	 * 本次分片上传的唯一标识
	 */
	private String uploadId;

	/**
	 * 已上传的分片列表
	 */
	private List<UploadPartVo> parts;

}
