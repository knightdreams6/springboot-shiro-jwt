package com.knight.entity.orm;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.knight.entity.base.BaseBean;
import io.swagger.annotations.ApiModel;
import lombok.Getter;
import lombok.Setter;

/**
 * <p>
 * 分片文件记录
 * </p>
 *
 * @author knight
 * @since 2023-06-29
 */
@Getter
@Setter
@TableName("multipart_files")
@ApiModel(value = "MultipartFiles对象", description = "分片文件记录")
public class MultipartFiles extends BaseBean {

	/** id */
	@TableId(value = "id")
	private String id;

	/** 桶名称 */
	private String mfBucketName;

	/** 对象名称 */
	private String mfObjectName;

	/** 上传唯一id */
	private String mfUploadId;

	/** 文件的hash值 */
	private String mfHash;

	/** 文件大小 */
	private Integer mfSize;

	/** 分片数量 */
	private Integer mfChunks;

	/** 0待合并1成功2失败 */
	private Integer mfState;

}
