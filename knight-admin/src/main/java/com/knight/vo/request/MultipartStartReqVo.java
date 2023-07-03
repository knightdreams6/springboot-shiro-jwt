package com.knight.vo.request;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 分片上传开始请求参数vo
 *
 * @author knight
 * @since 2023/06/29
 */
@Data
@ApiModel(value = "分片上传开始请求参数vo")
public class MultipartStartReqVo {

	@ApiModelProperty(value = "文件名称", required = true)
	private String fileName;

	@ApiModelProperty(value = "文件hash", required = true)
	private String fileHash;

	@ApiModelProperty(value = "分片数量", required = true)
	private Integer chunks;

	@ApiModelProperty(value = "文件大小", required = true)
	private Integer size;

}
