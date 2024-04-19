package com.knight.entity.orm;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.knight.entity.base.BaseBean;
import lombok.Getter;
import lombok.Setter;

/**
 * <p>
 * 分片块文件记录
 * </p>
 *
 * @author knight
 * @since 2023-06-29
 */
@Getter
@Setter
@TableName("multipart_chunk_files")
public class MultipartChunkFiles extends BaseBean {

	/** id */
	@TableId(value = "id")
	private String id;

	/** 桶名称 */
	private String mcfBucketName;

	/** 对象名称 */
	private String mcfObjectName;

	/** 上传唯一id */
	private String mcfUploadId;

	/** 分片位置 */
	private Integer mcfPartNumber;

	/** 分片大小 */
	private Integer mcfPartSize;

	/** 分片eTag */
	private String mcfETag;

}
