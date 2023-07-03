package com.knight.vo.request;

import io.swagger.annotations.ApiModelProperty;
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

	@ApiModelProperty(value = "上传id", required = true)
	private String uploadId;

	@ApiModelProperty(value = "分片数", required = true)
	private Integer partNumber;

	@ApiModelProperty(value = "分片文件", required = true)
	private MultipartFile partFile;

}
