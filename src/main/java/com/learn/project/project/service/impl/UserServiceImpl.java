package com.learn.project.project.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.learn.project.common.enums.RoleEnums;
import com.learn.project.common.utils.CommonsUtils;
import com.learn.project.framework.shiro.service.PermissionsService;
import com.learn.project.project.entity.User;
import com.learn.project.project.mapper.UserMapper;
import com.learn.project.project.service.IUserService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.time.LocalDateTime;

/**
 * <p>
 *  用户服务实现类
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

    @Override
    public User selectUserByPhone(String phone) {
        return userMapper.selectOne(new QueryWrapper<User>().eq("phone", phone));
    }

    /**
     * 用户注册,默认密码为手机号后六位
     * @param phone phone
     */
    @Transactional(rollbackFor = Exception.class)
    @Override
    public void register(String phone, String... args){
        User user = new User();
        user.setPhone(phone);
        user.setRegisterTime(LocalDateTime.now());
        // 如果有密码，则使用用户输入的密码
        String salt = CommonsUtils.uuid();
        String encryptPassword;
        if(args.length > 0){
            encryptPassword = CommonsUtils.encryptPassword(args[0], salt);
        }else{
            encryptPassword = CommonsUtils.encryptPassword(phone.substring(5, 11), salt);
        }
        user.setPassword(encryptPassword);
        user.setSalt(salt);
        userMapper.insert(user);
        permissionsService.addRole(user.getUserId(), RoleEnums.ADMIN.getCode(), RoleEnums.COMMON.getCode());
    }

}
