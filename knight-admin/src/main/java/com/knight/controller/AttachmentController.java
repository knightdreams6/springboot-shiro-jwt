package com.knight.controller;

import com.knight.entity.base.R;
import com.knight.storage.template.StorageTemplate;
import com.knight.storage.vo.response.CreateMultipartUploadVo;
import com.knight.storage.vo.response.OssUploadR;
import com.knight.storage.vo.response.UploadPartVo;
import com.knight.vo.request.MultipartStartReqVo;
import com.knight.vo.request.UploadPartReqVo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

/**
 * 附件控制器
 *
 * @author knight
 * @since 2023/01/15
 */
@Api(tags = "【attachment】附件")
@RestController
@RequestMapping("/attachment")
@RequiredArgsConstructor
public class AttachmentController {

	/**
	 * 存储模板
	 */
	private final StorageTemplate storageTemplate;

	@ApiOperation(value = "上传附件")
	@PostMapping
	@RequiresPermissions(value = "attachment:insert")
	public R<OssUploadR> upload(@RequestPart MultipartFile attachment) {
		return storageTemplate.upload(attachment);
	}

	@ApiOperation(value = "下载附件")
	@GetMapping
	@RequiresPermissions(value = "attachment:get")
	public R<String> download(@RequestParam String objectName) {
		return storageTemplate.getUrl(objectName);
	}

	@ApiOperation(value = "删除附件")
	@DeleteMapping
	@RequiresPermissions(value = "attachment:remove")
	public R<Object> delete(@RequestParam String objectName) {
		return storageTemplate.remove(objectName);
	}

	@ApiOperation(value = "开始分片上传")
	@PostMapping("/multipart/start")
	@RequiresPermissions(value = "attachment:insert")
	public R<CreateMultipartUploadVo> multipartStart(@RequestBody MultipartStartReqVo multipartStartReqVo) {
		return storageTemplate.initiateMultipartUpload(multipartStartReqVo.getFileName(),
				multipartStartReqVo.getFileHash(), multipartStartReqVo.getChunks(), multipartStartReqVo.getSize());
	}

	@ApiOperation(value = "分片上传")
	@PostMapping("/multipart/part")
	@RequiresPermissions(value = "attachment:insert")
	public R<UploadPartVo> multipartUpload(UploadPartReqVo uploadPartReqVo) {
		return storageTemplate.uploadPart(uploadPartReqVo.getUploadId(), uploadPartReqVo.getPartNumber(),
				uploadPartReqVo.getPartFile());
	}

	@ApiOperation(value = "完成分片上传")
	@PostMapping("/multipart/complete")
	@RequiresPermissions(value = "attachment:insert")
	public R<Object> multipartComplete(@RequestParam String uploadId) {
		return storageTemplate.completeMultipartUpload(uploadId);
	}

}
