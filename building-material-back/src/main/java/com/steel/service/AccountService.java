package com.steel.service;

import com.steel.common.Result;
import com.steel.entity.Right;
import com.steel.entity.User;

import java.util.List;

/**
 * @ProjectName: building-material
 * @Package: com.steel.service
 * @ClassName: AccountService
 * @Author: Hero
 * @Description: 账户服务
 * @Date: 2020/3/10 9:11
 * @Version: 1.0
 */
public interface AccountService {

    List<User> queryByLimitAccount(String username, Integer offset, Integer limit);

    Integer queryByLimitAccountCount(String username);

    Result deleteAccount(Integer[] ids);

    Result addAccount(String username, String nickname);

    Result updateAccount(User user);

    List<Right> queryRightByUserId(Integer userId);

    Result updateAccountRight(Integer userId, Integer[] checkedValue);
}
