package com.learn.project.project.controller;

import com.learn.project.common.enums.ErrorState;
import com.learn.project.common.utils.FileUtils;
import com.learn.project.framework.Result;
import io.swagger.annotations.Api;
import org.apache.shiro.authz.annotation.RequiresRoles;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

/**
 * @author lixiao
 * @date 2019/12/17 18:09
 */
@Api(tags = "【test】测试用户token方式登录")
@RestController
@RequestMapping("/test")
public class TestController {

    @GetMapping
    @RequiresRoles("role1")
    public Result test(){
        return Result.success("ok", "ok");
    }

    @PostMapping("/upload")
    @RequiresRoles("role1")
    public Result upload(MultipartFile file){
        if(file == null || file.isEmpty()){
            return Result.error(ErrorState.MISSING_PARAMETER);
        }
        FileUtils.upload(file, "D:/");
        return Result.success();
    }
}
