package com.steel.controller;

import com.steel.common.Result;
import com.steel.common.VerifyToken;
import com.steel.entity.Navigation;
import com.steel.service.NavigationService;
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
 * @ClassName: NavigationController
 * @Author: Hero
 * @Description: 导航controller
 * @Date: 2020/3/9 19:12
 * @Version: 1.0
 */
@Api("系统导航接口")
@RestController
@RequestMapping(value = "/navigation")
public class NavigationController {

    @Autowired
    private NavigationService navigationService;

    @ApiOperation(value = "查询", notes="按条件查询")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "navigationName", value = "导航名称", paramType = "query", required = false, dataType = "String"),
            @ApiImplicitParam(name = "pageIndex", value = "pageIndex", paramType = "query", required = true, dataType = "Integer"),
            @ApiImplicitParam(name = "pageSize", value = "pageSize", paramType = "query", required = true, dataType = "Integer")
    })
    @VerifyToken
    @RequestMapping(value = "/queryByLimitNavigation", method = RequestMethod.GET)
    public Result queryNavigation(String navigationName, Integer pageIndex, Integer pageSize) {
        return new Result("1001", "查询成功", this.navigationService.queryAllByLimit(navigationName, (pageIndex-1)*pageSize, pageSize), this.navigationService.queryByLimitCount(navigationName));
    }

    @VerifyToken
    @RequestMapping(value = "/editNavigation", method = RequestMethod.POST)
    public Result editNavigation(Navigation navigation){
        return this.navigationService.update(navigation);
    }

    @VerifyToken
    @RequestMapping(value = "/addNavigation", method = RequestMethod.POST)
    public Result addNavigation(Navigation navigation) {
        return this.navigationService.insert(navigation);
    }

    @VerifyToken
    @RequestMapping(value = "/deleteNavigation", method = RequestMethod.GET)
    public Result deleteNavigation(Integer[] ids) {
        return new Result("1001", "删除成功", this.navigationService.deleteNavigation(ids));
    }

}
