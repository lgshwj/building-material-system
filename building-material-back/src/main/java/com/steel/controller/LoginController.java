package com.steel.controller;

import com.steel.common.Result;
import com.steel.common.VerifyToken;
import com.steel.service.UserService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

/**
 * @ProjectName: building-material
 * @Package: com.steel.controller
 * @ClassName: LoginController
 * @Author: Hero
 * @Description: 登录
 * @Date: 2020/2/21 22:13
 * @Version: 1.0
 */

@Api("登录注册接口")
@RestController
@RequestMapping(value = "/auth")
public class LoginController {

    @Autowired
    private UserService userService;

    @ApiOperation(value = "登录", notes="登录系统")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "userName", value = "账号", paramType = "query", required = true, dataType = "String"),
            @ApiImplicitParam(name = "password", value = "密码", paramType = "query", required = true, dataType = "String")
    })
    @RequestMapping(value = "/login", method = RequestMethod.POST)
    public Result login(String userName, String password) {
        return this.userService.checkUser(userName, password);
    }


    @VerifyToken
    @RequestMapping(value = "/checkPermission", method = RequestMethod.GET)
    public Result checkPermission(String token, String navigationRoute){
        return this.userService.checkPermission(token, navigationRoute);
    }

    @VerifyToken
    @RequestMapping(value = "/updatePassword", method = RequestMethod.POST)
    public Result updatePassword(String token, String oldPassword, String newPassword) {
        return this.userService.updatePassword(token, oldPassword, newPassword);
    }
}
