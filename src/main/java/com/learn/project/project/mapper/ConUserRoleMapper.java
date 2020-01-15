package com.learn.project.project.mapper;

import com.learn.project.project.pojo.ConUserRole;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;

import java.util.Collection;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author knight
 * @since 2019-12-17
 */
public interface ConUserRoleMapper extends BaseMapper<ConUserRole> {

    /**
     * 根据userId获取所有roleId
     * @param userId userId
     * @return roleIds
     */
    Collection<String> selectRoleNamesByUserId(Integer userId);
}
