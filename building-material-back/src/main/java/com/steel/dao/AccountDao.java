package com.steel.dao;

import com.steel.entity.Right;
import com.steel.entity.User;
import io.swagger.models.auth.In;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @ProjectName: building-material
 * @Package: com.steel.dao
 * @ClassName: AccountDao
 * @Author: Hero
 * @Description: 账户dao
 * @Date: 2020/3/10 9:15
 * @Version: 1.0
 */
@Mapper
public interface AccountDao {

    List<User> queryByLimitAccountName(String username, Integer offset, Integer limit);

    Integer queryByLimitAccountNameCount(@Param("username")String username);

    Integer deleteAccount(Integer[] ids);

    List<Right> queryRightByUserId(Integer userId);

    int deleteAccountRight(Integer userId);

    int insertAccountRight(Integer[] navId, Integer userId);

    int queryPermission(Integer userId, String navigationRoute);
}
