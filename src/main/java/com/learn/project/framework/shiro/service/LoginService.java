package com.learn.project.framework.shiro.service;

import com.learn.project.common.constant.Constant;
import com.learn.project.common.constant.RedisKey;
import com.learn.project.common.enums.ErrorState;
import com.learn.project.common.enums.LoginType;
import com.learn.project.common.enums.RoleEnums;
import com.learn.project.common.utils.CommonsUtils;
import com.learn.project.framework.web.domain.Result;
import com.learn.project.framework.redis.RedisCache;
import com.learn.project.framework.shiro.token.CustomizedToken;
import com.learn.project.project.entity.User;
import com.learn.project.project.service.IUserService;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.ExpiredCredentialsException;
import org.apache.shiro.authc.IncorrectCredentialsException;
import org.apache.shiro.authc.UnknownAccountException;
import org.apache.shiro.subject.Subject;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.TimeUnit;

/**
 * @author lixiao
 * @version 1.0
 * @date 2020/4/15 16:50
 */
@Service
public class LoginService {

    @Resource
    private IUserService userService;
    
    @Resource
    private RedisCache redisCache;

    @Resource
    private TokenService tokenService;

    @Resource
    private PermissionsService permissionsService;

    
    public void sendLoginCode(String phone){
        // 这里使用默认值，随机验证码的方法为CommonsUtils.getCode()
        int code = 6666;
        // todo 此处为发送验证码代码
        // 将验证码加密后存储到redis中
        String encryptCode = CommonsUtils.encryptPassword(String.valueOf(code), phone);
        redisCache.setCacheObject(RedisKey.getLoginCodeKey(phone), encryptCode, Constant.CODE_EXPIRE_TIME, TimeUnit.MINUTES);
    }

    
    public void sendModifyPasswordCode(String phone) {
        int code = 6666;
        // todo 此处为发送验证码代码
        redisCache.setCacheObject(RedisKey.getModifyPasswordCodeKey(phone), code, Constant.CODE_EXPIRE_TIME, TimeUnit.MINUTES);
    }

    
    public Result loginByPassword(String phone, String password) {
        // 1.获取Subject
        Subject subject = SecurityUtils.getSubject();
        // 2.封装用户数据
        CustomizedToken token = new CustomizedToken(phone, password, LoginType.PASSWORD_LOGIN_TYPE.toString());
        // 3.执行登录方法
        try{
            subject.login(token);
            Map<String, Object> data = returnLoginInitParam(phone);
            return Result.success(data);
        }catch (UnknownAccountException e) {
            return Result.error(ErrorState.USERNAME_NOT_EXIST);
        } catch (IncorrectCredentialsException e){
            return Result.error(ErrorState.PASSWORD_ERROR);
        }
    }

    
    public Result loginByCode(String phone, String code) {
        // 1.获取Subject
        Subject subject = SecurityUtils.getSubject();
        User sysUser = userService.selectUserByPhone(phone);
        // 2.验证码登录，如果该用户不存在则创建该用户
        if (Objects.isNull(sysUser)) {
            // 2.1 注册
            this.register(phone);
        }
        // 3.封装用户数据
        CustomizedToken token = new CustomizedToken(phone, code, LoginType.CODE_LOGIN_TYPE.toString());
        // 4.执行登录方法
        try{
            subject.login(token);
            Map<String, Object> data = returnLoginInitParam(phone);
            return Result.success(data);
        }catch (UnknownAccountException e) {
            return Result.error(ErrorState.USERNAME_NOT_EXIST);
        }catch (ExpiredCredentialsException e){
            return Result.error(ErrorState.CODE_EXPIRE);
        } catch (IncorrectCredentialsException e){
            return Result.error(ErrorState.CODE_ERROR);
        }
    }

    
    public Result modifyPassword(String phone, String code, String password) {
        String redisCode = redisCache.getCacheObject(RedisKey.getModifyPasswordCodeKey(phone)).toString();
        if(!Objects.equals(code, redisCode)){
            return Result.error(ErrorState.CODE_ERROR);
        }
        User user = userService.selectUserByPhone(phone);
        // 如果用户不存在，执行注册
        if(Objects.isNull(user)){
            this.register(phone, password);
            return Result.success(this.returnLoginInitParam(phone));
        }
        String salt = CommonsUtils.uuid();
        String encryptPassword = CommonsUtils.encryptPassword(password, salt);
        user.setSalt(salt);
        user.setPassword(encryptPassword);
        // 删除缓存
        redisCache.deleteObject(RedisKey.getLoginUserKey(phone));
        userService.updateById(user);
        return Result.success(this.returnLoginInitParam(phone));
    }


    /**
     * 返回登录后初始化参数
     * @param phone phone
     * @return Map<String, Object>
     */
    private Map<String, Object> returnLoginInitParam(String phone) {
        Map<String, Object> data = new HashMap<>(1);
        User user = userService.selectUserByPhone(phone);
        // 生成jwtToken
        String token = tokenService.createToken(phone, user.getUserId(), user.getPassword());
        // token
        data.put("token", token);
        return data;
    }

    /**
     * 用户注册,默认密码为手机号后六位
     * @param phone phone
     */
    private void register(String phone, String... args){
        User user = new User();
        user.setPhone(phone);
        user.setRegisterTime(LocalDateTime.now());
        // 如果有密码，则使用用户输入的密码
        String salt = CommonsUtils.uuid();
        if(args.length > 0){
            String encryptPassword = CommonsUtils.encryptPassword(args[0], salt);
            user.setPassword(encryptPassword);
            user.setSalt(salt);
            userService.save(user);
            permissionsService.addRole(user.getUserId(), RoleEnums.ADMIN.getCode(), RoleEnums.COMMON.getCode());
        }else{
            String encryptPassword = CommonsUtils.encryptPassword(phone.substring(5, 11), salt);
            user.setSalt(salt);
            user.setPassword(encryptPassword);
            userService.save(user);
            permissionsService.addRole(user.getUserId(), RoleEnums.ADMIN.getCode(), RoleEnums.COMMON.getCode());
        }
    }
}
