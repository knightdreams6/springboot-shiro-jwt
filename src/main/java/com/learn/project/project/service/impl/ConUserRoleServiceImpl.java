package com.learn.project.project.service.impl;

import com.learn.project.project.pojo.ConUserRole;
import com.learn.project.project.mapper.ConUserRoleMapper;
import com.learn.project.project.service.IConUserRoleService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Collection;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author knight
 * @since 2019-12-17
 */
@Service
public class ConUserRoleServiceImpl extends ServiceImpl<ConUserRoleMapper, ConUserRole> implements IConUserRoleService {

    @Resource
    private ConUserRoleMapper userRoleMapper;

    @Override
    @Cacheable(value = "roles", key = "#userId")
    public Collection<String> selectRoleNamesByUserId(Integer userId) {
        return userRoleMapper.selectRoleNamesByUserId(userId);
    }
}
