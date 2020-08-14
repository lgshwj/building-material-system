package com.steel.service;

import com.steel.common.Result;
import com.steel.entity.Navigation;

import java.util.List;

/**
 * (TbNavigation)表服务接口
 *
 * @author makejava
 * @since 2020-03-09 16:34:14
 */
public interface NavigationService {

    /**
     * 通过ID查询单条数据
     *
     * @param id 主键
     * @return 实例对象
     */
    Navigation queryById(Integer id);

    /**
     * 查询多条数据
     *
     * @param offset 查询起始位置
     * @param limit 查询条数
     * @return 对象列表
     */
    List<Navigation> queryAllByLimit(String navigationName, int offset, int limit);

    /**
     * 新增数据
     *
     * @param tbNavigation 实例对象
     * @return 实例对象
     */
    Result insert(Navigation tbNavigation);

    /**
     * 修改数据
     *
     * @param tbNavigation 实例对象
     * @return 实例对象
     */
    Result update(Navigation tbNavigation);

    /**
     * 通过主键删除数据
     *
     * @param id 主键
     * @return 是否成功
     */
    boolean deleteById(Integer id);

    Integer queryByLimitCount(String navigationName);

    Result deleteNavigation(Integer[] ids);

    List<Navigation> queryAllNavigation();
}