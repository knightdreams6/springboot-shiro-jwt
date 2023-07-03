package com.knight.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.knight.entity.orm.MultipartFiles;
import com.knight.mapper.MultipartFilesMapper;
import com.knight.service.IMultipartFilesService;
import org.springframework.stereotype.Service;

/**
 * 分片文件记录 服务实现类
 *
 * @author knight
 * @since 2023-06-29
 */
@Service
public class MultipartFilesServiceImpl extends ServiceImpl<MultipartFilesMapper, MultipartFiles>
		implements IMultipartFilesService {

}
