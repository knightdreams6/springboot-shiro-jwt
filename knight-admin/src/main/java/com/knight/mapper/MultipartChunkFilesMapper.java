package com.knight.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.knight.entity.orm.MultipartChunkFiles;
import org.apache.ibatis.annotations.Mapper;

/**
 * 分片块文件记录 数据库访问层
 *
 * @author knight
 * @since 2023-06-29
 */
@Mapper
public interface MultipartChunkFilesMapper extends BaseMapper<MultipartChunkFiles> {

}
