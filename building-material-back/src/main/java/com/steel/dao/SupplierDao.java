package com.steel.dao;

import com.steel.entity.Supplier;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import java.util.List;

/**
 * (Supplier)表数据库访问层
 *
 * @author makejava
 * @since 2020-03-31 11:21:49
 */
@Mapper
public interface SupplierDao {

    /**
     * 通过ID查询单条数据
     *
     * @param id 主键
     * @return 实例对象
     */
    Supplier queryById(Integer id);

    /**
     * 查询指定行数据
     *
     * @param offset 查询起始位置
     * @param limit 查询条数
     * @return 对象列表
     */
    List<Supplier> queryAllByLimit(@Param("offset") int offset, @Param("limit") int limit);


    /**
     * 通过实体作为筛选条件查询
     *
     * @param supplier 实例对象
     * @return 对象列表
     */
    List<Supplier> queryAll(Supplier supplier);

    /**
     * 新增数据
     *
     * @param supplier 实例对象
     * @return 影响行数
     */
    int insert(Supplier supplier);

    /**
     * 修改数据
     *
     * @param supplier 实例对象
     * @return 影响行数
     */
    int update(Supplier supplier);

    /**
     * 通过主键删除数据
     *
     * @param id 主键
     * @return 影响行数
     */
    int deleteById(Integer id);


    List<Supplier> querySupplierByLimit(@Param("supplier")Supplier supplier, @Param("offset")Integer offset, @Param("limit")Integer limit);

    Integer querySupplierByLimitCount(Supplier supplier);

    Integer deleteSupplier(Integer[] ids);

    List<Supplier> querySupplierAll();
}