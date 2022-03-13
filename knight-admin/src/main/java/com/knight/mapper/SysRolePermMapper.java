package com.knight.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.knight.entity.orm.SysRolePerm;
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
