package com.learn.project.project.controller;

import com.learn.project.common.utils.ServletUtils;
import com.learn.project.framework.shiro.service.TokenService;
import com.learn.project.framework.web.domain.LoginUser;
import com.learn.project.framework.web.domain.Result;
import com.learn.project.project.service.IUserService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.shiro.authz.annotation.Logical;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.apache.shiro.authz.annotation.RequiresRoles;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

/**
 * <p>
 *  用户前端控制器
 * </p>
 *
 * @author knight
 * @since 2019-12-17
 */
@Api(tags = "【user】用户")
@RestController
@RequestMapping("/user")
@Validated
public class UserController {

    @Resource
    private IUserService userService;

    @Resource
    private TokenService tokenService;


    @ApiOperation("获取当前用户基本信息")
    @GetMapping("/info")
    @RequiresRoles(value = {"admin", "common"}, logical = Logical.OR)
    public Result info() {
        LoginUser loginUser = tokenService.getLoginUser(ServletUtils.getRequest());
        return Result.success(loginUser);
    }


    @ApiOperation("退出")
    @PostMapping("/logout")
    @RequiresRoles(value = {"admin", "common"}, logical = Logical.OR)
    public Result logout() {
        // 退出操作
        return Result.success();
    }


    @ApiOperation("获取所有用户")
    @GetMapping
    @RequiresRoles("admin")
    public Result user() {
        return Result.success("userList", userService.list());
    }

    @ApiOperation("获取所有用户")
    @GetMapping("/list")
    @RequiresPermissions("system:user:list")
    public Result users() {
        return Result.success("userList", userService.list());
    }

}

