package com.steel.dao;

import com.steel.entity.Evaluate;
import com.steel.entity.Factory;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import java.util.List;

/**
 * (Factory)表数据库访问层
 *
 * @author makejava
 * @since 2020-03-31 20:43:23
 */
@Mapper
public interface FactoryDao {

    /**
     * 通过ID查询单条数据
     *
     * @param id 主键
     * @return 实例对象
     */
    Factory queryById(Integer id);

    /**
     * 查询指定行数据
     *
     * @param offset 查询起始位置
     * @param limit 查询条数
     * @return 对象列表
     */
    List<Factory> queryAllByLimit(@Param("offset") int offset, @Param("limit") int limit);


    /**
     * 通过实体作为筛选条件查询
     *
     * @param factory 实例对象
     * @return 对象列表
     */
    List<Factory> queryAll(Factory factory);

    /**
     * 新增数据
     *
     * @param factory 实例对象
     * @return 影响行数
     */
    int insert(Factory factory);

    /**
     * 修改数据
     *
     * @param factory 实例对象
     * @return 影响行数
     */
    int update(Factory factory);

    /**
     * 通过主键删除数据
     *
     * @param id 主键
     * @return 影响行数
     */
    int deleteById(Integer id);


    List<Factory> queryFactoryWihtLimit(@Param("factory") Factory factory, @Param("offset")Integer offset, @Param("limit")Integer limit);

    Integer queryFactoryCountWihtLimit(Factory factory);

    Integer deleteFactory(Integer[] ids);

    List<Factory> queryFactoryAll();



}