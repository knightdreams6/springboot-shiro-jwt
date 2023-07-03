package com.knight.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.knight.entity.orm.MultipartChunkFiles;
import com.knight.mapper.MultipartChunkFilesMapper;
import com.knight.service.IMultipartChunkFilesService;
import org.springframework.stereotype.Service;

/**
 * 分片块文件记录 服务实现类
 *
 * @author knight
 * @since 2023-06-29
 */
@Service
public class MultipartChunkFilesServiceImpl extends ServiceImpl<MultipartChunkFilesMapper, MultipartChunkFiles>
		implements IMultipartChunkFilesService {

}
