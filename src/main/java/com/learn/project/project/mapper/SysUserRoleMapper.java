package com.learn.project.project.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.learn.project.project.entity.SysUserRole;
import org.apache.ibatis.annotations.Mapper;

/**
 * <p>
 * 用户和角色关联表 Mapper 接口
 * </p>
 *
 * @author knight
 * @since 2021-11-23
 */
@Mapper
public interface SysUserRoleMapper extends BaseMapper<SysUserRole> {

}
