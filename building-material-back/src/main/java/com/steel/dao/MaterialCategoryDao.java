package com.steel.dao;

import com.steel.entity.MaterialCategory;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import java.util.List;

/**
 * (MaterialCategory)表数据库访问层
 *
 * @author makejava
 * @since 2020-03-13 17:01:27
 */
@Mapper
public interface MaterialCategoryDao {

    /**
     * 通过ID查询单条数据
     *
     * @param id 主键
     * @return 实例对象
     */
    MaterialCategory queryById(Integer id);

    MaterialCategory queryByCategoryName(String materialCategoryName);

    MaterialCategory queryByCategoryNameNotSelf(@Param("materialCategoryName") String materialCategoryName, @Param("id")Integer id);

    /**
     * 查询指定行数据
     *
     * @param offset 查询起始位置
     * @param limit 查询条数
     * @return 对象列表
     */
    List<MaterialCategory> queryAllByLimit(@Param("materialCategoryName") String materialCategoryName, @Param("offset") int offset, @Param("limit") int limit);

    Integer queryAllByLimitCount(@Param("materialCategoryName") String materialCategoryName);
    /**
     * 通过实体作为筛选条件查询
     *
     * @param materialCategory 实例对象
     * @return 对象列表
     */
    List<MaterialCategory> queryAll(MaterialCategory materialCategory);

    /**
     * 新增数据
     *
     * @param materialCategory 实例对象
     * @return 影响行数
     */
    int insert(MaterialCategory materialCategory);

    /**
     * 修改数据
     *
     * @param materialCategory 实例对象
     * @return 影响行数
     */
    int update(MaterialCategory materialCategory);

    /**
     * 通过主键删除数据
     *
     * @param id 主键
     * @return 影响行数
     */
    int deleteById(Integer id);

    int deleteMaterialCategory(Integer[] ids);

    List<MaterialCategory> queryCategoryAll();
}