package com.knight.controller;

import com.knight.entity.base.R;
import com.knight.storage.template.StorageTemplate;
import com.knight.storage.vo.response.CreateMultipartUploadVo;
import com.knight.storage.vo.response.OssUploadR;
import com.knight.storage.vo.response.UploadPartVo;
import com.knight.vo.request.MultipartStartReqVo;
import com.knight.vo.request.UploadPartReqVo;
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
@RestController
@RequestMapping("/attachment")
@RequiredArgsConstructor
public class AttachmentController {

	/**
	 * 存储模板
	 */
	private final StorageTemplate storageTemplate;

	/**
	 * 上传附件
	 * @param attachment 附件
	 * @return R<OssUploadR>
	 */
	@PostMapping
	@RequiresPermissions(value = "attachment:insert")
	public R<OssUploadR> upload(@RequestPart MultipartFile attachment) {
		return storageTemplate.upload(attachment);
	}

	/**
	 * 下载附件
	 * @param objectName objectName
	 * @return R<String>
	 */
	@GetMapping
	@RequiresPermissions(value = "attachment:get")
	public R<String> download(@RequestParam String objectName) {
		return storageTemplate.getUrl(objectName);
	}

	/**
	 * 删除附件
	 * @param objectName objectName
	 * @return R<Void>
	 */
	@DeleteMapping
	@RequiresPermissions(value = "attachment:remove")
	public R<Void> delete(@RequestParam String objectName) {
		return storageTemplate.remove(objectName);
	}

	/**
	 * 开始分片上传
	 * @param multipartStartReqVo MultipartStartReqVo
	 * @return R<CreateMultipartUploadVo>
	 */
	@PostMapping("/multipart/start")
	@RequiresPermissions(value = "attachment:insert")
	public R<CreateMultipartUploadVo> multipartStart(@RequestBody MultipartStartReqVo multipartStartReqVo) {
		return storageTemplate.initiateMultipartUpload(multipartStartReqVo.getFileName(),
				multipartStartReqVo.getFileHash(), multipartStartReqVo.getChunks(), multipartStartReqVo.getSize());
	}

	/**
	 * 分片上传
	 * @param uploadPartReqVo UploadPartReqVo
	 * @return R<UploadPartVo>
	 */
	@PostMapping("/multipart/part")
	@RequiresPermissions(value = "attachment:insert")
	public R<UploadPartVo> multipartUpload(UploadPartReqVo uploadPartReqVo) {
		return storageTemplate.uploadPart(uploadPartReqVo.getUploadId(), uploadPartReqVo.getPartNumber(),
				uploadPartReqVo.getPartFile());
	}

	/**
	 * 完成分片上传
	 * @param uploadId 上传id
	 * @return R<UploadPartVo>
	 */
	@PostMapping("/multipart/complete")
	@RequiresPermissions(value = "attachment:insert")
	public R<Void> multipartComplete(@RequestParam String uploadId) {
		return storageTemplate.completeMultipartUpload(uploadId);
	}

}
