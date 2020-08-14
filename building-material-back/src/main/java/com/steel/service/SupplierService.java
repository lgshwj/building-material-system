package com.steel.service;

import com.steel.common.Result;
import com.steel.entity.Supplier;

import java.util.List;

/**
 * (Supplier)表服务接口
 *
 * @author makejava
 * @since 2020-03-31 11:21:49
 */
public interface SupplierService {

    /**
     * 通过ID查询单条数据
     *
     * @param id 主键
     * @return 实例对象
     */
    Supplier queryById(Integer id);

    /**
     * 查询多条数据
     *
     * @param offset 查询起始位置
     * @param limit 查询条数
     * @return 对象列表
     */
    List<Supplier> queryAllByLimit(int offset, int limit);

    /**
     * 新增数据
     *
     * @param supplier 实例对象
     * @return 实例对象
     */
    Result insert(Supplier supplier);

    /**
     * 修改数据
     *
     * @param supplier 实例对象
     * @return 实例对象
     */
    Result update(Supplier supplier);

    /**
     * 通过主键删除数据
     *
     * @param ids 主键
     * @return 是否成功
     */
    Result deleteById(Integer[] ids);


    Result querySupplierByLimit(Supplier supplier, Integer pageIndex, Integer pageSize);

    Result querySupplierAll();


}