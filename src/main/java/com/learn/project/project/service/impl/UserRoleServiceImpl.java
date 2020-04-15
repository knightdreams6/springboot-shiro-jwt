package com.learn.project.project.service.impl;

import com.learn.project.project.entity.UserRole;
import com.learn.project.project.mapper.UserRoleMapper;
import com.learn.project.project.service.IUserRoleService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 用户和角色关联表 服务实现类
 * </p>
 *
 * @author lixiao
 * @since 2020-04-15
 */
@Service
public class UserRoleServiceImpl extends ServiceImpl<UserRoleMapper, UserRole> implements IUserRoleService {

}
