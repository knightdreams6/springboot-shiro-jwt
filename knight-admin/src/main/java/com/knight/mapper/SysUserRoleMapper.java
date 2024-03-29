package com.knight.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.knight.entity.orm.SysUserRole;
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
