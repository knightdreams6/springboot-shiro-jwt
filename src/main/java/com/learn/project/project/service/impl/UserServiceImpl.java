package com.learn.project.project.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.learn.project.common.enums.ErrorState;
import com.learn.project.common.enums.RoleEnums;
import com.learn.project.common.utils.CommonsUtils;
import com.learn.project.framework.shiro.service.PermissionsService;
import com.learn.project.framework.web.exception.ServiceException;
import com.learn.project.project.entity.User;
import com.learn.project.project.mapper.UserMapper;
import com.learn.project.project.service.IUserService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.support.TransactionTemplate;

import javax.annotation.Resource;

/**
 * <p>
 * 用户服务实现类
 * </p>
 *
 * @author knight
 * @since 2019-12-17
 */
@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements IUserService {

    @Resource
    private UserMapper userMapper;

    @Resource
    private PermissionsService permissionsService;

    @Resource
    private TransactionTemplate transactionTemplate;

    @Override
    public User selectUserByPhone(String phone) {
        return userMapper.selectOne(new QueryWrapper<User>().eq("phone", phone));
    }

    /**
     * 用户注册,默认密码为手机号后六位
     *
     * @param phone phone
     */
    @Override
    public Boolean register(String phone, String... args) {
        // 判断是否已存在该用户
        User db = this.selectUserByPhone(phone);
        if (db != null) {
            throw new ServiceException(ErrorState.USER_ALREADY_EXIST.getMsg());
        }
        User user = new User();
        user.setPhone(phone);
        // 如果有密码，则使用用户输入的密码
        String salt = CommonsUtils.uuid();
        String encryptPassword;
        if (args.length > 0) {
            encryptPassword = CommonsUtils.encryptPassword(args[0], salt);
        } else {
            encryptPassword = CommonsUtils.encryptPassword(phone.substring(5, 11), salt);
        }
        user.setPassword(encryptPassword);
        user.setSalt(salt);
        return transactionTemplate.execute(status -> {
            try {
                userMapper.insert(user);
                permissionsService.addRole(user.getUserId(), RoleEnums.ADMIN.getCode(), RoleEnums.COMMON.getCode());
                return Boolean.TRUE;
            } catch (Exception e) {
                //回滚
                status.setRollbackOnly();
                return Boolean.FALSE;
            }
        });
    }

}
