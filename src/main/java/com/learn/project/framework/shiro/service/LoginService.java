package com.learn.project.framework.shiro.service;

import com.aliyuncs.exceptions.ClientException;
import com.learn.project.common.constant.Constant;
import com.learn.project.common.constant.RedisKey;
import com.learn.project.common.enums.ErrorState;
import com.learn.project.common.enums.LoginType;
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


    public boolean sendLoginCode(String phone) throws ClientException {
        // 这里使用默认值，随机验证码的方法为CommonsUtils.getCode()
        int code = 6666;
        // todo 此处为发送验证码代码
//        SmsUtils.sendSms(phone, code);
        // 将验证码加密后存储到redis中
        String encryptCode = CommonsUtils.encryptPassword(String.valueOf(code), phone);
        redisCache.setCacheObject(RedisKey.getLoginCodeKey(phone), encryptCode, Constant.CODE_EXPIRE_TIME, TimeUnit.MINUTES);
        return true;
    }


    public boolean sendModifyPasswordCode(String phone) throws ClientException {
        int code = 6666;
        // todo 此处为发送验证码代码
//        SmsUtils.sendSms(phone, code);
        redisCache.setCacheObject(RedisKey.getModifyPasswordCodeKey(phone), code, Constant.CODE_EXPIRE_TIME, TimeUnit.MINUTES);
        return true;
    }


    public Result loginByPassword(String phone, String password) {
        // 1.获取Subject
        Subject subject = SecurityUtils.getSubject();
        // 2.封装用户数据
        CustomizedToken token = new CustomizedToken(phone, password, LoginType.PASSWORD_LOGIN_TYPE.toString());
        // 3.执行登录方法
        try{
            subject.login(token);
            return Result.success(returnLoginInitParam(phone));
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
            userService.register(phone);
        }
        // 3.封装用户数据
        CustomizedToken token = new CustomizedToken(phone, code, LoginType.CODE_LOGIN_TYPE.toString());
        // 4.执行登录方法
        try{
            subject.login(token);
            return Result.success(returnLoginInitParam(phone));
        }catch (UnknownAccountException e) {
            return Result.error(ErrorState.USERNAME_NOT_EXIST);
        }catch (ExpiredCredentialsException e){
            return Result.error(ErrorState.CODE_EXPIRE);
        } catch (IncorrectCredentialsException e){
            return Result.error(ErrorState.CODE_ERROR);
        }
    }


    public Result modifyPassword(String phone, String code, String password) {
        Object modifyCode = redisCache.getCacheObject(RedisKey.getModifyPasswordCodeKey(phone));
        // 判断redis中是否存在验证码
        if(Objects.isNull(modifyCode)){
            return Result.error(ErrorState.CODE_EXPIRE);
        }
        // 判断redis中code与传递过来的code 是否相等
        if(!Objects.equals(code, modifyCode.toString())){
            return Result.error(ErrorState.CODE_ERROR);
        }
        User user = userService.selectUserByPhone(phone);
        // 如果用户不存在，执行注册
        if(Objects.isNull(user)){
            Boolean flag = userService.register(phone, password);
           if(flag){

               return Result.success(this.returnLoginInitParam(phone));
           }else {
               return Result.error();
           }
        }
        String salt = CommonsUtils.uuid();
        String encryptPassword = CommonsUtils.encryptPassword(password, salt);
        user.setSalt(salt);
        user.setPassword(encryptPassword);
        // 删除缓存
        redisCache.deleteObject(RedisKey.getLoginUserKey(phone));
        boolean flag = userService.updateById(user);
        if(flag){
            return Result.success(this.returnLoginInitParam(phone));
        }else {
            return Result.error();
        }
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

}
