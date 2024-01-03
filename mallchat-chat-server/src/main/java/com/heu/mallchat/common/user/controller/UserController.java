package com.heu.mallchat.common.user.controller;


import com.heu.mallchat.common.common.domain.dto.RequestInfo;
import com.heu.mallchat.common.common.domain.vo.resp.ApiResult;
import com.heu.mallchat.common.common.interceptor.TokenInterceptor;
import com.heu.mallchat.common.common.utils.RequestHolder;
import com.heu.mallchat.common.user.domain.vo.req.ModifyNameReq;
import com.heu.mallchat.common.user.domain.vo.resp.UserInfoResp;
import com.heu.mallchat.common.user.service.UserService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import org.springframework.stereotype.Controller;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

/**
 * <p>
 * 用户表 前端控制器
 * </p>
 *
 * @author <a href="https://github.com/leeleezl">lizzzz</a>
 * @since 2023-11-20
 */
@RestController
@RequestMapping("/capi/user")
@Api(tags = "用户相关接口")
public class UserController {

    @Autowired
    private UserService userService;

    @GetMapping("/userInfo")
    @ApiOperation("获取用户信息")
    public ApiResult<UserInfoResp> getUserInfo() {
        return ApiResult.success(userService.getUserInfo(RequestHolder.get().getUid()));
    }

    @PutMapping("/name")
    @ApiOperation("修改用户名")
    public ApiResult<Void> modifyName(@Valid @RequestBody ModifyNameReq req) {
        userService.modifyName(RequestHolder.get().getUid(), req.getName());
        return ApiResult.success();
    }

}

