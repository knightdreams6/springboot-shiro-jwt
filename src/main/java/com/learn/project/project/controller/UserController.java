package com.learn.project.project.controller;

import com.learn.project.framework.web.domain.Result;
import com.learn.project.project.service.IUserService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
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
@Api(tags = "【user】需要权限校验的接口")
@RestController
@RequestMapping("/user")
@Validated
public class UserController {

    @Resource
    private IUserService userService;

    @ApiOperation("获取所有用户")
    @GetMapping
    @RequiresRoles("admin")
    public Result user() {
        return Result.success(userService.list());
    }

    @ApiOperation("获取所有用户")
    @GetMapping("/list")
    @RequiresPermissions("system:user:list")
    public Result users() {
        return Result.success(userService.list());
    }

}

