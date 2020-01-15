package com.learn.project.project.service;

import com.learn.project.project.pojo.ConUserRole;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.Collection;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author knight
 * @since 2019-12-17
 */
public interface IConUserRoleService extends IService<ConUserRole> {

    /**
     * 根据userId获取所有roleId
     * @param userId userId
     * @return roleIds
     */
    Collection<String> selectRoleNamesByUserId(Integer userId);

    /**
     * 关联用户与角色
     * @param userId 用户id
     * @param roleId 角色id
     */
    default void connectUserRole(Integer userId, Integer roleId){
        ConUserRole userRole = new ConUserRole();
        userRole.setUserId(userId);
        userRole.setRoleId(roleId);
        this.save(userRole);
    }
}
