package com.learn.project.project.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.learn.project.project.entity.SysRolePerm;
import org.apache.ibatis.annotations.Mapper;

/**
 * <p>
 * 角色和菜单关联表 Mapper 接口
 * </p>
 *
 * @author knight
 * @since 2021-11-23
 */
@Mapper
public interface SysRolePermMapper extends BaseMapper<SysRolePerm> {

}
