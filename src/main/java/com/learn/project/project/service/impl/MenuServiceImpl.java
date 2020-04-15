package com.learn.project.project.service.impl;

import com.learn.project.entity.Menu;
import com.learn.project.mapper.MenuMapper;
import com.learn.project.service.IMenuService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 菜单权限表 服务实现类
 * </p>
 *
 * @author lixiao
 * @since 2020-04-15
 */
@Service
public class MenuServiceImpl extends ServiceImpl<MenuMapper, Menu> implements IMenuService {

}
