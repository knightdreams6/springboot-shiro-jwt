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
     * 用户注册,默认密码为手机号后六位
     * @param phone phone
     * @param args 密码
     * @return boolean
     */
    Boolean register(String phone, String... args);

}
