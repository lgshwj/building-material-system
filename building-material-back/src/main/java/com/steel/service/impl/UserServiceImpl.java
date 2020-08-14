package com.steel.service.impl;

import com.auth0.jwt.JWT;
import com.auth0.jwt.exceptions.JWTDecodeException;
import com.steel.common.Result;
import com.steel.common.TokenUtil;
import com.steel.dao.AccountDao;
import com.steel.dao.NavigationDao;
import com.steel.entity.Navigation;
import com.steel.entity.User;
import com.steel.dao.UserDao;
import com.steel.service.NavigationService;
import com.steel.service.UserService;
import io.swagger.models.auth.In;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import javax.rmi.CORBA.Util;
import java.util.HashMap;
import java.util.Map;

/**
 * (User)表服务实现类
 *
 * @author makejava
 * @since 2020-02-21 22:00:36
 */
@Service("userService")
public class UserServiceImpl implements UserService {

    @Autowired
    private UserDao userDao;

    @Autowired
    private NavigationDao navigationDao;

    @Autowired
    private AccountDao accountDao;

    private BCryptPasswordEncoder bCryptPasswordEncoder;

    public UserServiceImpl() {
        bCryptPasswordEncoder = new BCryptPasswordEncoder();
    }

    /**
     * 通过ID查询单条数据
     *
     * @param id 主键
     * @return 实例对象
     */
    @Override
    public User queryById(Integer id) {
        return this.userDao.queryById(id);
    }

    /**
     * 新增数据
     *
     * @param user 实例对象
     * @return 实例对象
     */
    @Override
    public User insert(User user) {
        this.userDao.insert(user);
        return user;
    }

    /**
     * 修改数据
     *
     * @param user 实例对象
     * @return 实例对象
     */
    @Override
    public User update(User user) {
        this.userDao.update(user);
        return this.queryById(user.getId());
    }

    /**
     * @Description 验证
     * @param userName
     * @param password
     * @Return com.steel.common.Result
     * @Exception
     */
    @Override
    public Result checkUser(String userName, String password) {
        User user = userDao.queryByUserName(userName);
        if (user == null) {
            return new Result("2001", "用户不存在");
        }

        if (bCryptPasswordEncoder.matches(password, user.getPassword())) {
            Map<String,String> map = new HashMap<>();
            map.put("username", userName);
            map.put("token", TokenUtil.getToken(user.getId().toString(),user.getPassword()));
            return new Result("1001", "登录成功", map);
        } else {
            return new Result("2001", "密码错误");
        }
    }

    @Override
    public Result checkPermission(String token, String navigationRoute) {
        String userId = TokenUtil.getInfoFromToken(token, "id");
        String password = TokenUtil.getInfoFromToken(token, "password");
        if (userId == null || password == null || !TokenUtil.verify(token)) {
            return new Result("3001", "登录已失效，请重新登录");
        }

        int one = this.accountDao.queryPermission(Integer.parseInt(userId), navigationRoute);
        if (one > 0) {
            return new Result("1001", "拥有权限");
        } else {
            return new Result("2001", "非法权限");
        }
    }

    @Override
    public Result updatePassword(String token, String oldPassword, String newPassword) {
        /*Integer userId = 0;
        try {
            userId = Integer.parseInt(JWT.decode(token).getAudience().get(0));
        } catch (JWTDecodeException j) {
            return new Result("3001", "token非法，请重新登录");
        }*/
        String userId = TokenUtil.getInfoFromToken(token, "id");
        String password = TokenUtil.getInfoFromToken(token, "password");
        if (userId == null || password == null || !TokenUtil.verify(token)) {
            return new Result("3001", "登录已失效，请重新登录");
        }

        // 先去验证原密码正确与否
        User user = this.userDao.queryById(Integer.parseInt(userId));
        if (user != null) {
            // 原密码验证正确
            if (bCryptPasswordEncoder.matches(oldPassword, user.getPassword())) {
                newPassword = bCryptPasswordEncoder.encode(newPassword);
                User newUser = new User();
                newUser.setId(Integer.parseInt(userId));
                newUser.setPassword(newPassword);
                if (this.userDao.update(newUser) == 1) {
                    return new Result("1001", "修改成功，请重新登录");
                } else {
                    return new Result("2001", "修改失败");
                }
            } else {
                return new Result("2001", "原密码错误");
            }
        } else {
            return new Result("3001", "登录已失效，请重新登录");
        }
    }
}