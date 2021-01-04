package com.learn.project.project.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.learn.project.project.entity.Role;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.Set;

/**
 * <p>
 * 角色信息表 Mapper 接口
 * </p>
 *
 * @author lixiao
 * @since 2020-04-15
 */
public interface RoleMapper extends BaseMapper<Role> {

    /**
     * 根据用户id获取其角色key列表
     *
     * @param userId userId
     * @return Set<Role>
     */
    Set<String> getRoleSet(@Param("userId") Integer userId);


    /**
     * 根据用户id获取其角色id列表
     *
     * @param userId userId
     * @return Set<Integer>
     */
    @Select("SELECT role_id FROM sys_user_role WHERE user_id = #{userId}")
    Set<Integer> getRoleIdSet(Integer userId);


    /**
     * 给用户添加角色
     *
     * @param userId  用户id
     * @param roleIds 需要添加的角色
     */
    void addRole(@Param("userId") Integer userId, @Param("roleIds") Integer[] roleIds);
}
