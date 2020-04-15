package com.learn.project.project.service.impl;

import com.learn.project.entity.RoleMenu;
import com.learn.project.mapper.RoleMenuMapper;
import com.learn.project.service.IRoleMenuService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 角色和菜单关联表 服务实现类
 * </p>
 *
 * @author lixiao
 * @since 2020-04-15
 */
@Service
public class RoleMenuServiceImpl extends ServiceImpl<RoleMenuMapper, RoleMenu> implements IRoleMenuService {

}
