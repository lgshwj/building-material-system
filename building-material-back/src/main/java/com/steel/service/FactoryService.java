package com.steel.service;

import com.steel.common.Result;
import com.steel.entity.Factory;
import java.util.List;

/**
 * (Factory)表服务接口
 *
 * @author makejava
 * @since 2020-03-31 20:43:23
 */
public interface FactoryService {

    /**
     * 通过ID查询单条数据
     *
     * @param id 主键
     * @return 实例对象
     */
    Factory queryById(Integer id);

    /**
     * 查询多条数据
     *
     * @param offset 查询起始位置
     * @param limit 查询条数
     * @return 对象列表
     */
    List<Factory> queryAllByLimit(int offset, int limit);

    /**
     * 新增数据
     *
     * @param factory 实例对象
     * @return 实例对象
     */
    Result insert(Factory factory);

    /**
     * 修改数据
     *
     * @param factory 实例对象
     * @return 实例对象
     */
    Result update(Factory factory);

    /**
     * 通过主键删除数据
     *
     * @param id 主键
     * @return 是否成功
     */
    boolean deleteById(Integer id);

    Result<Factory> queryFactoryWithLimit(Factory factory, Integer pageIndex, Integer pageSize);

    Result deleteById(Integer[] ids);

    Result queryFactoryAll();


}