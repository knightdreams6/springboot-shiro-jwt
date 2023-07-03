package com.knight.storage.vo.response;

import lombok.Builder;
import lombok.Data;

/**
 * 上传分片vo
 *
 * @author knight
 * @since 2023/06/29
 */
@Builder
@Data
public class UploadPartVo {

	/**
	 * 分片号
	 */
	private int partNumber;

	/**
	 * etag
	 */
	private String etag;

}
