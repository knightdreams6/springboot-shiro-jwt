package com.learn.project.project.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.learn.project.project.entity.Perm;
import org.apache.ibatis.annotations.Param;

import java.util.Set;

/**
 * <p>
 * 菜单权限表 Mapper 接口
 * </p>
 *
 * @author lixiao
 * @since 2020-04-15
 */
public interface PermMapper extends BaseMapper<Perm> {

    /**
     * 根据角色id获取其权限
     *
     * @param roleId 角色id
     * @return Set<String>
     */
    Set<String> getPermissionsSet(@Param("roleId") Integer roleId);
}
