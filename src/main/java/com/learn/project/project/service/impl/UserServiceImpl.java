package com.learn.project.project.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.learn.project.common.constant.RedisKey;
import com.learn.project.framework.redis.RedisCache;
import com.learn.project.project.entity.User;
import com.learn.project.project.mapper.UserMapper;
import com.learn.project.project.service.IUserService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.concurrent.TimeUnit;

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
    private RedisCache redisCache;

    @Override
    public User selectUserByPhone(String phone) {
        String userKey = RedisKey.getUserKey(phone);
        Object cacheObject = redisCache.getCacheObject(userKey);
        if (cacheObject == null) {
            User db = userMapper.selectOne(new QueryWrapper<User>().eq("phone", phone));
            redisCache.setCacheObject(RedisKey.getUserKey(phone), db, 30, TimeUnit.SECONDS);
            return db;
        }
        return (User) cacheObject;
    }

}
