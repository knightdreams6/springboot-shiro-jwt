package com.learn.project.project.service;

import com.aliyuncs.exceptions.ClientException;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.learn.project.framework.Result;
import com.learn.project.project.pojo.User;
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
    default User selectUserByPhone(String phone){
        return this.getOne(new QueryWrapper<User>().eq("phone", phone));
    }

    /**
     * 发送注册验证码
     * @param phone phone
     * @throws ClientException 阿里云短信异常
     * @return 发送是否成功
     */
    Result sendLoginCode(String phone) throws ClientException;


    /**
     * 发送修改密码验证码
     * @param phone phone
     * @throws ClientException 阿里云短信异常
     * @return 发送是否成功
     */
    Result sendModifyPasswordCode(String phone) throws ClientException;

    /**
     * 密码登录方式
     * @param phone phone
     * @param password password
     * @return Result
     */
    Result loginByPassword(String phone, String password);


    /**
     * 验证码登录方式
     * @param phone phone
     * @param code code
     * @return Result
     */
    Result loginByCode(String phone, String code);

    /**
     * 修改手机号
     * @param phone phone
     * @param code code
     * @param password password
     * @return 修改结果
     */
    Result modifyPassword(String phone, String code, String password);
}
