package com.learn.project.project.service.impl;

import com.learn.project.project.entity.Role;
import com.learn.project.project.mapper.RoleMapper;
import com.learn.project.project.service.IRoleService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 角色信息表 服务实现类
 * </p>
 *
 * @author lixiao
 * @since 2020-04-15
 */
@Service
public class RoleServiceImpl extends ServiceImpl<RoleMapper, Role> implements IRoleService {

}
