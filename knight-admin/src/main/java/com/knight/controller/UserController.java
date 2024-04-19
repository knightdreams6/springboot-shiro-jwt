package com.knight.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.knight.api.version.annotation.ApiVersion;
import com.knight.entity.base.R;
import com.knight.entity.orm.SysUser;
import com.knight.service.ISysUserService;
import com.knight.shiro.service.TokenService;
import com.knight.valid.annotation.PhoneNumber;
import com.knight.vo.response.SysUserVo;
import com.knight.vo.response.UserBasicInfoVo;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.apache.shiro.authz.annotation.RequiresRoles;
import org.apache.shiro.authz.annotation.RequiresUser;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 用户
 *
 * @author knight
 */
@ApiVersion
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
	 * 获取当前用户基本信息
	 * @return R<UserBasicInfoVo>
	 */
	@RequiresUser
	@GetMapping("/info")
	public R<UserBasicInfoVo> info() {
		return R.vo(tokenService.getLoginUser(), UserBasicInfoVo::new);
	}

	/**
	 * 获取当前用户基本信息V2
	 * @return R<UserBasicInfoVo>
	 */
	@ApiVersion(2)
	@RequiresUser
	@GetMapping("/info")
	public R<UserBasicInfoVo> infoV2() {
		return R.vo(tokenService.getLoginUser(), UserBasicInfoVo::new);
	}

	/**
	 * 注册
	 * @param phone 手机号
	 * @return R<Void>
	 */
	@PostMapping("/register")
	public R<Void> register(@PhoneNumber String phone) {
		return R.bool(userService.register(phone));
	}

	/**
	 * 删除用户
	 * @param userId 用户id
	 * @return R<Void>
	 */
	@RequiresPermissions("system:user:remove")
	@DeleteMapping("/{userId}")
	public R<Void> deleted(@PathVariable @NotNull(message = "userId不能为空") Integer userId) {
		return R.bool(userService.removeById(userId));
	}

	/**
	 * 修改用户
	 * @param user SysUser
	 * @return R<Void>
	 */
	@RequiresPermissions("system:user:update")
	@PutMapping
	public R<Void> update(@RequestBody SysUser user) {
		return R.bool(userService.updateById(user));
	}

	/**
	 * 获取所有用户 [system:user:list]权限
	 * @return R<List<SysUserVo>>
	 */
	@RequiresPermissions(value = { "system:user:list" })
	@GetMapping
	public R<List<SysUserVo>> users() {
		return R.list(userService.list(), SysUserVo::new);
	}

	/**
	 * 分页获取用户 [admin]角色权限
	 * @param pageNum 页数
	 * @param pageSize 每页数量
	 * @return R<IPage<SysUserVo>>
	 */
	@RequiresRoles(value = { "admin" })
	@GetMapping("/page")
	public R<IPage<SysUserVo>> user(@NotNull Integer pageNum, @NotNull Integer pageSize) {
		IPage<SysUser> page = new Page<>(pageNum, pageSize);
		return R.page(userService.page(page), SysUserVo::new);
	}

}
