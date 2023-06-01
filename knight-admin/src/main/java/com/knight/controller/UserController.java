package com.knight.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.knight.entity.base.R;
import com.knight.entity.orm.SysUser;
import com.knight.service.ISysUserService;
import com.knight.shiro.service.TokenService;
import com.knight.valid.annotation.PhoneNumber;
import com.knight.vo.response.SysUserVo;
import com.knight.vo.response.UserBasicInfoVo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.apache.shiro.authz.annotation.RequiresRoles;
import org.apache.shiro.authz.annotation.RequiresUser;
import org.springframework.messaging.simp.user.SimpUser;
import org.springframework.messaging.simp.user.SimpUserRegistry;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.constraints.NotNull;
import java.security.Principal;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

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

	/**
	 * 用户服务
	 */
	private final ISysUserService userService;

	/**
	 * 令牌服务
	 */
	private final TokenService tokenService;

	/**
	 * websocket用户注册表
	 */
	private final SimpUserRegistry simpUserRegistry;

	@RequiresRoles("admin")
	@ApiOperation(value = "获取链接的websocket用户列表")
	@GetMapping("/ws/list")
	@ResponseBody
	public R<Set<Principal>> websocketUsers() {
		return R.ok(simpUserRegistry.getUsers().stream().map(SimpUser::getPrincipal).collect(Collectors.toSet()));
	}

	@RequiresUser
	@ApiOperation(value = "获取当前用户基本信息")
	@GetMapping("/info")
	public R<UserBasicInfoVo> info() {
		return R.vo(tokenService.getLoginUser(), UserBasicInfoVo::new);
	}

	@ApiOperation("添加用户")
	@ApiImplicitParam(name = "phone", value = "手机号", paramType = "query", dataTypeClass = String.class)
	@PostMapping("/register")
	public R<Object> register(@PhoneNumber String phone) {
		return R.bool(userService.register(phone));
	}

	@RequiresPermissions("system:user:remove")
	@ApiOperation("删除用户")
	@DeleteMapping("/{userId}")
	public R<Object> deleted(@PathVariable @NotNull(message = "userId不能为空") Integer userId) {
		return R.bool(userService.removeById(userId));
	}

	@RequiresPermissions("system:user:update")
	@ApiOperation("修改用户")
	@PutMapping
	public R<Object> update(SysUser user) {
		return R.bool(userService.updateById(user));
	}

	@RequiresPermissions(value = { "system:user:list" })
	@ApiOperation(value = "获取所有用户 [system:user:list]权限")
	@GetMapping
	public R<List<SysUserVo>> users() {
		return R.list(userService.list(), SysUserVo::new);
	}

	@RequiresRoles(value = { "admin" })
	@ApiOperation(value = "分页获取用户 [admin]角色权限")
	@GetMapping("/page")
	public R<IPage<SysUserVo>> user(@NotNull Integer pageNum, @NotNull Integer pageSize) {
		IPage<SysUser> page = new Page<>(pageNum, pageSize);
		return R.page(userService.page(page), SysUserVo::new);
	}

}
