package com.knight.controller;

import com.knight.entity.base.R;
import com.knight.storage.template.StorageTemplate;
import com.knight.storage.vo.response.OssUploadR;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
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

}
