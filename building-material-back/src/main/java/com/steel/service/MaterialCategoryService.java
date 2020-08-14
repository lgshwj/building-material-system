package com.steel.service;

import com.steel.common.Result;
import com.steel.entity.MaterialCategory;
import java.util.List;

/**
 * (MaterialCategory)表服务接口
 *
 * @author makejava
 * @since 2020-03-13 17:01:27
 */
public interface MaterialCategoryService {

    /**
     * 通过ID查询单条数据
     *
     * @param id 主键
     * @return 实例对象
     */
    MaterialCategory queryById(Integer id);

    /**
     * 查询多条数据
     *
     * @param offset 查询起始位置
     * @param limit 查询条数
     * @return 对象列表
     */
    Result queryAllByLimit(String materialCategoryName, int offset, int limit);

    /**
     * 新增数据
     *
     * @param materialCategory 实例对象
     * @return 实例对象
     */
    Result insert(MaterialCategory materialCategory);

    /**
     * 修改数据
     *
     * @param materialCategory 实例对象
     * @return 实例对象
     */
    Result update(MaterialCategory materialCategory);

    /**
     * 通过主键删除数据
     *
     * @param id 主键
     * @return 是否成功
     */
    boolean deleteById(Integer id);

    Result deleteMaterialCategory(Integer[] ids);

    Result queryCategoryAll();
}