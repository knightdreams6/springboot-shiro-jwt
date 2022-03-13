package com.knight.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.knight.entity.orm.SysUser;
import org.apache.ibatis.annotations.Mapper;

/**
 * <p>
 * 用户信息 Mapper 接口
 * </p>
 *
 * @author knight
 * @since 2021-11-23
 */
@Mapper
public interface SysUserMapper extends BaseMapper<SysUser> {

}
