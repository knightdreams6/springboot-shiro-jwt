package com.knight.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.knight.entity.base.LoginUser;
import com.knight.entity.base.Result;
import com.knight.entity.orm.SysUser;
import com.knight.service.ISysUserService;
import com.knight.shiro.service.TokenService;
import com.knight.valid.annotation.PhoneNumber;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.apache.shiro.authz.annotation.RequiresRoles;
import org.apache.shiro.authz.annotation.RequiresUser;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.constraints.NotNull;

/**
 * <p>
 * 用户前端控制器
 * </p>
 *
 * @author knight
 * @since 2019-12-17
 */
@Api(tags = "【user】用户")
@RestController
@RequestMapping("/user")
@Validated
@RequiredArgsConstructor
public class UserController extends BaseController {

	/** 用户服务 */
	private final ISysUserService userService;

	/** 令牌服务 */
	private final TokenService tokenService;

	@ApiOperation("添加用户")
	@ApiImplicitParam(name = "phone", value = "手机号", paramType = "query", dataTypeClass = String.class)
	@PostMapping("/register")
	public Result register(@PhoneNumber String phone) {
		return super.result(userService.register(phone));
	}

	@RequiresUser
	@ApiOperation(value = "获取当前用户基本信息")
	@GetMapping("/info")
	public Result info() {
		LoginUser loginUser = tokenService.getLoginUser();
		return Result.success(loginUser);
	}

	@RequiresPermissions("system:user:remove")
	@ApiOperation("删除用户")
	@DeleteMapping("/{userId}")
	public Result deleted(@PathVariable @NotNull(message = "userId不能为空") Integer userId) {
		return super.result(userService.removeById(userId));
	}

	@RequiresPermissions("system:user:update")
	@ApiOperation("修改用户")
	@PutMapping("/{userId}")
	public Result update(SysUser user) {
		return super.result(userService.updateById(user));
	}

	@RequiresPermissions(value = { "system:user:list" })
	@ApiOperation(value = "获取所有用户 [system:user:list]权限")
	@GetMapping
	public Result users() {
		return Result.success(userService.list());
	}

	@RequiresRoles(value = { "admin" })
	@ApiOperation(value = "分页获取用户 [admin]角色权限")
	@GetMapping("/page")
	public Result user(@NotNull Integer pageNum, @NotNull Integer pageSize) {
		IPage<SysUser> page = new Page<>(pageNum, pageSize);
		return Result.success(userService.page(page));
	}

}
