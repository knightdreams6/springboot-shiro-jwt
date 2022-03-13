package com.knight.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.knight.entity.orm.SysRole;
import org.apache.ibatis.annotations.Mapper;

/**
 * <p>
 * 角色信息 Mapper 接口
 * </p>
 *
 * @author knight
 * @since 2021-11-23
 */
@Mapper
public interface SysRoleMapper extends BaseMapper<SysRole> {

}
