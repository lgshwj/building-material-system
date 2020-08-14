package com.steel.controller;

import com.steel.common.Result;
import com.steel.common.VerifyToken;
import com.steel.entity.Navigation;
import com.steel.entity.Right;
import com.steel.entity.User;
import com.steel.service.AccountService;
import com.steel.service.NavigationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @ProjectName: building-material
 * @Package: com.steel.controller
 * @ClassName: AccountController
 * @Author: Hero
 * @Description: 账户controller
 * @Date: 2020/3/9 23:30
 * @Version: 1.0
 */

@RestController
@RequestMapping(value = "/account")
public class AccountController {

    @Autowired
    private AccountService accountService;

    @Autowired
    private NavigationService navigationService;

    @VerifyToken
    @RequestMapping(value = "/queryByLimitAccountName", method = RequestMethod.GET)
    public Result queryByLimitAccountName(String username, Integer pageIndex, Integer pageSize) {
        return new Result("1001", "查询成功", this.accountService.queryByLimitAccount(username, (pageIndex-1)*pageSize, pageSize), this.accountService.queryByLimitAccountCount(username));
    }

    @VerifyToken
    @RequestMapping(value = "/deleteAccount", method = RequestMethod.GET)
    public Result deleteAccount(Integer[] ids) {
        return this.accountService.deleteAccount(ids);
    }

    @VerifyToken
    @RequestMapping(value = "/addAccount", method = RequestMethod.POST)
    public Result addAccount(String username, String nickname) {
        return this.accountService.addAccount(username, nickname);
    }

    @VerifyToken
    @RequestMapping(value = "/updateAccount", method = RequestMethod.POST)
    public Result updateAccount(User user) {
        return this.accountService.updateAccount(user);
    }

    @VerifyToken
    @RequestMapping(value = "/getAccountRight", method = RequestMethod.GET)
    public Result getAccountRight(Integer userId) {
        List<Navigation> navigationList = navigationService.queryAllNavigation();
        List<Right> rightList = accountService.queryRightByUserId(userId);
        Map<String , List> map = new HashMap<>();
        map.put("navigation", navigationList);
        map.put("right", rightList);
        return new Result("1001", "查询成功", map);
    }

    @VerifyToken
    @RequestMapping(value = "/updateAccountRight", method = RequestMethod.POST)
    public Result updateAccountRight(Integer userId, Integer[] checkedValue) {
        return this.accountService.updateAccountRight(userId, checkedValue);
    }
}
