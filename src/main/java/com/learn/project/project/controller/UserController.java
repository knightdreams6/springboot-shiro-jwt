package com.learn.project.project.controller;

import com.learn.project.common.enums.ErrorState;
import com.learn.project.common.utils.ServletUtils;
import com.learn.project.framework.annotction.PhoneNumber;
import com.learn.project.framework.shiro.service.TokenService;
import com.learn.project.framework.web.domain.LoginUser;
import com.learn.project.framework.web.domain.Result;
import com.learn.project.framework.web.exception.ServiceException;
import com.learn.project.project.entity.User;
import com.learn.project.project.service.IUserService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
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

    @ApiOperation("用户注册")
    @ApiImplicitParam(name = "phone", value = "手机号", paramType = "query")
    @GetMapping("/register")
    public Result register(@PhoneNumber String phone) {
        User db = userService.selectUserByPhone(phone);
        if (db != null) {
            throw new ServiceException(ErrorState.USER_ALREADY_EXIST.getMsg());
        }
        userService.register(phone);
        return Result.success();
    }


    @ApiOperation(value = "获取当前用户基本信息", response = User.class)
    @GetMapping("/info")
    @RequiresRoles(value = {"admin", "common"}, logical = Logical.OR)
    public Result info() {
        LoginUser loginUser = tokenService.getLoginUser(ServletUtils.getRequest());
        return Result.success(loginUser);
    }


    @ApiOperation("退出")
    @PostMapping("/logout")
    public Result logout() {
        // 退出操作
        return Result.success();
    }


    @ApiOperation(value = "获取所有用户", response = User.class)
    @GetMapping
    @RequiresRoles("admin")
    public Result user() {
        return Result.success("userList", userService.list());
    }


    @ApiOperation(value = "获取所有用户", response = User.class)
    @GetMapping("/list")
    @RequiresPermissions("system:user:list")
    public Result users() {
        return Result.success("userList", userService.list());
    }

}

