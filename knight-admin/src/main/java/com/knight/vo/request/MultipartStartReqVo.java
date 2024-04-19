package com.knight.vo.request;

import lombok.Data;

/**
 * 分片上传开始请求参数vo
 *
 * @author knight
 * @since 2023/06/29
 */
@Data
public class MultipartStartReqVo {

	/**
	 * 文件名称
	 */
	private String fileName;

	/**
	 * 文件hash
	 */
	private String fileHash;

	/**
	 * 分片数量
	 */
	private Integer chunks;

	/**
	 * 文件大小
	 */
	private Integer size;

}
