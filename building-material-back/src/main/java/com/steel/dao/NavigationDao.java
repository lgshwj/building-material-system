package com.steel.dao;

import com.steel.entity.Navigation;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import java.util.List;

/**
 * (TbNavigation)表数据库访问层
 *
 * @author makejava
 * @since 2020-03-09 16:34:14
 */
@Mapper
public interface NavigationDao {

    /**
     * 通过ID查询单条数据
     *
     * @param id 主键
     * @return 实例对象
     */
    Navigation queryById(Integer id);

    /**
     * 查询指定行数据
     *
     * @param offset 查询起始位置
     * @param limit 查询条数
     * @return 对象列表
     */
    List<Navigation> queryAllByLimit(@Param("navigationName") String navigationName, @Param("offset") int offset, @Param("limit") int limit);


    Integer queryByLimitCount(@Param("navigationName") String navigationName);

    /**
     * 通过实体作为筛选条件查询
     *
     *
     * @return 对象列表
     */
    List<Navigation> queryAll();

    /**
     * 新增数据
     *
     * @param tbNavigation 实例对象
     * @return 影响行数
     */
    int insert(Navigation tbNavigation);

    Integer deleteNavigation(Integer[] ids);

    /**
     * 修改数据
     *
     * @param tbNavigation 实例对象
     * @return 影响行数
     */
    int update(Navigation tbNavigation);

    int duplicateForNavigationRoute(@Param("navigationRoute") String navigationRoute, @Param("id") Integer id);

    /**
     * 通过主键删除数据
     *
     * @param id 主键
     * @return 影响行数
     */
    int deleteById(Integer id);



}