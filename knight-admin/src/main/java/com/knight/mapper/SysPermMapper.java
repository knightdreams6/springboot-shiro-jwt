package com.knight.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.knight.entity.orm.SysPerm;
import org.apache.ibatis.annotations.Mapper;

/**
 * <p>
 * 菜单权限表 Mapper 接口
 * </p>
 *
 * @author knight
 * @since 2021-11-23
 */
@Mapper
public interface SysPermMapper extends BaseMapper<SysPerm> {

}
