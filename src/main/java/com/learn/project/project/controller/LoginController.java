package com.learn.project.project.controller;

import com.aliyuncs.exceptions.ClientException;
import com.learn.project.common.enums.ErrorState;
import com.learn.project.common.annotction.RequestLimit;
import com.learn.project.framework.web.controller.BaseController;
import com.learn.project.framework.web.domain.Result;
import com.learn.project.common.annotction.PhoneNumber;
import com.learn.project.framework.shiro.service.LoginService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.validation.constraints.NotEmpty;

/**
 * @author lixiao
 * @version 1.0
 * @date 2020/4/15 16:51
 */
@Api(tags = "【user】登录")
@RestController
@RequestMapping("/login")
@Validated
public class LoginController extends BaseController {

    @Resource
    private LoginService loginService;


    @RequestLimit(second = 60 * 60 * 24, maxCount = 5, msg = ErrorState.REQUEST_LIMIT)
    @ApiOperation(value = "发送登录验证码")
    @ApiImplicitParam(name = "phone", value = "手机号", required = true, paramType = "query")
    @GetMapping("/code")
    public Result sendLoginCode(@PhoneNumber String phone) throws ClientException {
        return result(loginService.sendLoginCode(phone));
    }


    @RequestLimit(second = 60 * 60 * 24, maxCount = 5, msg = ErrorState.REQUEST_LIMIT)
    @ApiOperation("发送修改密码验证码")
    @ApiImplicitParam(name = "phone", value = "手机号", required = true, paramType = "query")
    @GetMapping("/modify-pwd-code")
    public Result sendModifyPasswordCode(@PhoneNumber String phone) throws ClientException {
        return result(loginService.sendModifyPasswordCode(phone));
    }


    @ApiOperation("通过手机验证码修改密码")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "phone", value = "手机号", required = true, paramType = "query", dataType = "string"),
            @ApiImplicitParam(name = "code", value = "验证码", required = true, paramType = "query", dataType = "string"),
            @ApiImplicitParam(name = "password", value = "密码", required = true, paramType = "query", dataType = "string")
    })
    @PutMapping("/password")
    public Result modifyPassword(@PhoneNumber String phone,
                                 @NotEmpty(message = "验证码不能为空") String code,
                                 @NotEmpty(message = "密码不能为空") String password){
        return loginService.modifyPassword(phone, code, password);
    }


    @ApiOperation("密码登录")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "phone", value = "手机号", required = true, paramType = "query", dataType = "string"),
            @ApiImplicitParam(name = "password", value = "密码", required = true, paramType = "query", dataType = "string")
    })
    @PostMapping("/password")
    public Result loginByPassword(@PhoneNumber String phone,
                                  @NotEmpty(message = "密码不能为空") String password){
        return loginService.loginByPassword(phone, password);
    }


    @ApiOperation("验证码登录")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "phone", value = "手机号", required = true, paramType = "query", dataType = "string"),
            @ApiImplicitParam(name = "code", value = "验证码", required = true, paramType = "query", dataType = "string"),
    })
    @PostMapping("/code")
    public Result loginByCode(@PhoneNumber String phone,
                              @NotEmpty(message = "验证码不能为空") String code){
        return loginService.loginByCode(phone, code);
    }

}
