package com.learn.project.project.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.learn.project.common.annotction.PhoneNumber;
import com.learn.project.common.annotction.RepeatSubmit;
import com.learn.project.framework.shiro.service.TokenService;
import com.learn.project.framework.web.controller.BaseController;
import com.learn.project.framework.web.domain.LoginUser;
import com.learn.project.framework.web.domain.Result;
import com.learn.project.project.entity.User;
import com.learn.project.project.service.IUserService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.apache.shiro.authz.annotation.RequiresRoles;
import org.apache.shiro.authz.annotation.RequiresUser;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.validation.constraints.NotNull;

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
public class UserController extends BaseController {

    @Resource
    private IUserService userService;

    @Resource
    private TokenService tokenService;


    @ApiOperation("添加用户")
    @ApiImplicitParam(name = "phone", value = "手机号", paramType = "query")
    @PostMapping("/register")
    @RepeatSubmit
    public Result register(@PhoneNumber String phone) {
        return super.result(userService.register(phone));
    }


    @RequiresUser
    @ApiOperation(value = "获取当前用户基本信息", response = User.class)
    @GetMapping("/info")
    public Result info() {
        LoginUser loginUser = tokenService.getLoginUser();
        return Result.success(loginUser);
    }


    @RequiresPermissions("system:user:remove")
    @ApiOperation("删除用户")
    @DeleteMapping("/{userId}")
    public Result deleted(@PathVariable @NotNull(message = "userId不能为空") Integer userId){
        return super.result(userService.removeById(userId));
    }


    @RequiresPermissions("system:user:update")
    @ApiOperation("修改用户")
    @PutMapping("/{userId}")
    public Result update(User user){
        return super.result(userService.updateById(user));
    }


    @RequiresPermissions(value = {"system:user:list"})
    @ApiOperation(value = "获取所有用户 [system:user:list]权限", response = User.class)
    @GetMapping
    public Result users() {
        return Result.success(userService.list());
    }


    @RequiresRoles(value = {"admin"})
    @ApiOperation(value = "分页获取用户 [admin]角色权限", response = User.class)
    @GetMapping("/page")
    public Result user(@NotNull Integer pageNum, @NotNull Integer pageSize) {
        IPage<User> page = new Page<>(pageNum, pageSize);
        return Result.success(userService.page(page));
    }

}

