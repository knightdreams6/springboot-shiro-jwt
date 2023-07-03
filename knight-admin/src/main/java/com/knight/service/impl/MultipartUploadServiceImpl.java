package com.knight.service.impl;

import cn.hutool.core.collection.CollUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.knight.entity.orm.MultipartChunkFiles;
import com.knight.entity.orm.MultipartFiles;
import com.knight.service.IMultipartChunkFilesService;
import com.knight.service.IMultipartFilesService;
import com.knight.storage.service.MultipartUploadService;
import com.knight.storage.vo.response.UploadPartVo;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * 多部分上传服务impl
 *
 * @author knight
 * @since 2023/06/29
 */
@Service
@RequiredArgsConstructor
public class MultipartUploadServiceImpl implements MultipartUploadService {

	/**
	 * 文件服务
	 */
	private final IMultipartFilesService filesService;

	/**
	 * 块文件服务
	 */
	private final IMultipartChunkFilesService chunkFilesService;

	@Override
	public String selectUploadIdByFileHash(String fileHash) {
		LambdaQueryWrapper<MultipartFiles> queryWrapper = Wrappers.<MultipartFiles>lambdaQuery()
			// 查询上传id
			.select(MultipartFiles::getMfUploadId)
			// 根据文件hash过滤
			.eq(MultipartFiles::getMfHash, fileHash);
		MultipartFiles multipartFiles = filesService.getOne(queryWrapper);
		return multipartFiles == null ? null : multipartFiles.getMfUploadId();
	}

	@Override
	public boolean saveFile(String bucketName, String objectName, String fileHash, String uploadId, Integer chunks,
			Integer size) {
		LambdaQueryWrapper<MultipartFiles> queryWrapper = Wrappers.<MultipartFiles>lambdaQuery()
			// 查询id
			.select(MultipartFiles::getId)
			// 根据文件hash过滤
			.eq(MultipartFiles::getMfHash, fileHash);
		MultipartFiles db = filesService.getOne(queryWrapper);

		MultipartFiles multipartFiles = new MultipartFiles();
		if (Objects.nonNull(db)) {
			multipartFiles.setId(db.getId());
		}
		multipartFiles.setMfBucketName(bucketName);
		multipartFiles.setMfObjectName(objectName);
		multipartFiles.setMfHash(fileHash);
		multipartFiles.setMfUploadId(uploadId);
		multipartFiles.setMfChunks(chunks);
		multipartFiles.setMfSize(size);
		return filesService.saveOrUpdate(multipartFiles);
	}

	@Override
	public boolean savePart(String bucketName, String objectName, String uploadId, Integer partNumber, Integer partSize,
			String etag) {
		MultipartChunkFiles multipartChunkFiles = new MultipartChunkFiles();
		multipartChunkFiles.setMcfBucketName(bucketName);
		multipartChunkFiles.setMcfObjectName(objectName);
		multipartChunkFiles.setMcfUploadId(uploadId);
		multipartChunkFiles.setMcfPartNumber(partNumber);
		multipartChunkFiles.setMcfPartSize(partSize);
		multipartChunkFiles.setMcfETag(etag);
		return chunkFilesService.save(multipartChunkFiles);
	}

	@Override
	public List<UploadPartVo> selectPartList(String uploadId) {
		LambdaQueryWrapper<MultipartChunkFiles> queryWrapper = Wrappers.<MultipartChunkFiles>lambdaQuery()
			.select(MultipartChunkFiles::getMcfPartNumber, MultipartChunkFiles::getMcfETag)
			// 根据上传id过滤
			.eq(MultipartChunkFiles::getMcfUploadId, uploadId);
		List<MultipartChunkFiles> chunkFiles = chunkFilesService.list(queryWrapper);
		if (CollUtil.isEmpty(chunkFiles)) {
			return Collections.emptyList();
		}
		return chunkFiles.stream()
			.map(chunkFile -> UploadPartVo.builder()
				.partNumber(chunkFile.getMcfPartNumber())
				.etag(chunkFile.getMcfETag())
				.build())
			.collect(Collectors.toList());
	}

	@Override
	public boolean complete(String uploadId) {
		LambdaQueryWrapper<MultipartFiles> queryWrapper = Wrappers.<MultipartFiles>lambdaQuery()
			// 查询id
			.select(MultipartFiles::getId)
			// 根据上传过滤
			.eq(MultipartFiles::getMfUploadId, uploadId)
			.last("limit 1");
		MultipartFiles multipartFiles = filesService.getOne(queryWrapper);
		if (Objects.isNull(multipartFiles)) {
			return false;
		}
		multipartFiles.setMfState(1);
		return filesService.updateById(multipartFiles);
	}

	@Override
	public String selectObjectNameByUploadId(String uploadId) {
		LambdaQueryWrapper<MultipartFiles> queryWrapper = Wrappers.<MultipartFiles>lambdaQuery()
			// 查询ObjectName
			.select(MultipartFiles::getMfObjectName)
			// 根据上传过滤
			.eq(MultipartFiles::getMfUploadId, uploadId)
			.last("limit 1");
		MultipartFiles multipartFiles = filesService.getOne(queryWrapper);
		if (Objects.isNull(multipartFiles)) {
			return null;
		}
		return multipartFiles.getMfObjectName();
	}

}
