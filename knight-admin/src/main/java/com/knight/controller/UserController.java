package com.knight.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.knight.entity.base.LoginUser;
import com.knight.entity.base.Result;
import com.knight.entity.orm.SysUser;
import com.knight.service.ISysUserService;
import com.knight.shiro.service.TokenService;
import com.knight.valid.annotation.PhoneNumber;
import com.knight.vo.SysUserVo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.apache.shiro.authz.annotation.RequiresRoles;
import org.apache.shiro.authz.annotation.RequiresUser;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.constraints.NotNull;
import java.util.List;

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
public class UserController {

	/** 用户服务 */
	private final ISysUserService userService;

	/** 令牌服务 */
	private final TokenService tokenService;

	@ApiOperation("添加用户")
	@ApiImplicitParam(name = "phone", value = "手机号", paramType = "query", dataTypeClass = String.class)
	@PostMapping("/register")
	public Result<Object> register(@PhoneNumber String phone) {
		return Result.bool(userService.register(phone));
	}

	@RequiresUser
	@ApiOperation(value = "获取当前用户基本信息")
	@GetMapping("/info")
	public Result<LoginUser> info() {
		LoginUser loginUser = tokenService.getLoginUser();
		return Result.success(loginUser);
	}

	@RequiresPermissions("system:user:remove")
	@ApiOperation("删除用户")
	@DeleteMapping("/{userId}")
	public Result<Object> deleted(@PathVariable @NotNull(message = "userId不能为空") Integer userId) {
		return Result.bool(userService.removeById(userId));
	}

	@RequiresPermissions("system:user:update")
	@ApiOperation("修改用户")
	@PutMapping
	public Result<Object> update(SysUser user) {
		return Result.bool(userService.updateById(user));
	}

	@RequiresPermissions(value = { "system:user:list" })
	@ApiOperation(value = "获取所有用户 [system:user:list]权限")
	@GetMapping
	public Result<List<SysUserVo>> users() {
		return Result.list(userService.list(), SysUserVo::new);
	}

	@RequiresRoles(value = { "admin" })
	@ApiOperation(value = "分页获取用户 [admin]角色权限")
	@GetMapping("/page")
	public Result<IPage<SysUserVo>> user(@NotNull Integer pageNum, @NotNull Integer pageSize) {
		IPage<SysUser> page = new Page<>(pageNum, pageSize);
		return Result.page(userService.page(page), SysUserVo::new);
	}

}
