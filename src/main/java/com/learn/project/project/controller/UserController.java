package com.learn.project.project.controller;


import com.aliyuncs.exceptions.ClientException;
import com.learn.project.framework.Result;
import com.learn.project.framework.annotction.PhoneNumber;
import com.learn.project.project.service.IUserService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.validation.Valid;
import javax.validation.constraints.NotEmpty;

/**
 * <p>
 *  用户前端控制器
 * </p>
 *
 * @author knight
 * @since 2019-12-17
 */
@Api(description = "登录")
@RestController
@RequestMapping("/user")
@Validated
public class UserController {

    @Resource
    private IUserService userService;

    @ApiOperation("发送登录验证码")
    @ApiImplicitParam(name = "phone", value = "手机号", required = true, paramType = "query")
    @GetMapping("/loginCode")
    public Result sendLoginCode(@Valid @PhoneNumber String phone) throws ClientException {
        return userService.sendLoginCode(phone);
    }

    @ApiOperation("发送修改密码验证码")
    @ApiImplicitParam(name = "phone", value = "手机号", required = true, paramType = "query")
    @GetMapping("/modifyPasswordCode")
    public Result sendModifyPasswordCode(@Valid @PhoneNumber String phone) throws ClientException {
        return userService.sendModifyPasswordCode(phone);
    }

    @ApiOperation("修改手机号")
    @ApiImplicitParam(name = "phone", value = "手机号", required = true, paramType = "query")
    @PutMapping("/modifyPassword")
    public Result modifyPassword(@Valid @PhoneNumber String phone, @NotEmpty(message = "验证码不能为空") String code, @NotEmpty(message = "密码不能为空") String password){
        return userService.modifyPassword(phone, code, password);
    }

    @ApiOperation("密码登录")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "phone", value = "手机号", required = true, paramType = "query"),
            @ApiImplicitParam(name = "password", value = "密码", required = true, paramType = "query")
    })
    @PostMapping("/login/password")
    public Result loginByPassword(@Valid @PhoneNumber String phone, @NotEmpty(message = "密码不能为空") String password){
        return userService.loginByPassword(phone, password);
    }

    @ApiOperation("验证码登录")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "phone", value = "手机号", required = true, paramType = "query"),
            @ApiImplicitParam(name = "code", value = "验证码", required = true, paramType = "query")
    })
    @PostMapping("/login/code")
    public Result loginByCode(@Valid @PhoneNumber String phone, @NotEmpty(message = "验证码不能为空") String code){
        return userService.loginByCode(phone, code);
    }

}

