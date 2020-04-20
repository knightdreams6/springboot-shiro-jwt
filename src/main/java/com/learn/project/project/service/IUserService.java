package com.learn.project.project.service;

import com.learn.project.project.entity.User;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * <p>
 *  用户服务类
 * </p>
 *
 * @author knight
 * @since 2019-12-17
 */
public interface IUserService extends IService<User> {


    /**
     * 根据手机号查询用户
     * @param phone phone
     * @return User
     */
    User selectUserByPhone(String phone);

    /**
     * 获取当前用户基本信息
     * @return user
     */
    User selectInfo();
}
