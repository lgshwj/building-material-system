package com.steel.service.impl;

import com.steel.common.Result;
import com.steel.entity.MaterialCategory;
import com.steel.dao.MaterialCategoryDao;
import com.steel.service.MaterialCategoryService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.List;

/**
 * (MaterialCategory)表服务实现类
 *
 * @author makejava
 * @since 2020-03-13 17:01:27
 */
@Service("materialCategoryService")
public class MaterialCategoryServiceImpl implements MaterialCategoryService {
    @Resource
    private MaterialCategoryDao materialCategoryDao;

    /**
     * 通过ID查询单条数据
     *
     * @param id 主键
     * @return 实例对象
     */
    @Override
    public MaterialCategory queryById(Integer id) {
        return this.materialCategoryDao.queryById(id);
    }

    /**
     * 查询多条数据
     *
     * @param offset 查询起始位置
     * @param limit 查询条数
     * @return 对象列表
     */
    @Override
    public Result queryAllByLimit(String materialCategoryName, int offset, int limit) {
        List<MaterialCategory> materialCategories = this.materialCategoryDao.queryAllByLimit(materialCategoryName, offset, limit);
        Integer count = this.materialCategoryDao.queryAllByLimitCount(materialCategoryName);

        return new Result("1001", "查询成功", materialCategories, count);
    }

    /**
     * 新增数据
     *
     * @param materialCategory 实例对象
     * @return 实例对象
     */
    @Override
    public Result insert(MaterialCategory materialCategory) {
        if (this.materialCategoryDao.queryByCategoryName(materialCategory.getMaterialCategoryName()) != null) {
            return new Result("2001", "该建筑材料类别已存在");
        }
        int one = this.materialCategoryDao.insert(materialCategory);
        if (one == 1) {
            return new Result("1001", "新增成功");
        }
        return new Result("2001", "新增失败");
    }

    /**
     * 修改数据
     *
     * @param materialCategory 实例对象
     * @return 实例对象
     */
    @Override
    public Result update(MaterialCategory materialCategory) {
        if (this.materialCategoryDao.queryByCategoryNameNotSelf(materialCategory.getMaterialCategoryName(), materialCategory.getId()) != null) {
            return new Result("2001","该建筑材料类别已存在");
        }

        if (this.materialCategoryDao.update(materialCategory) == 1) {
            return new Result("1001", "修改成功");
        }
        return new Result("2001", "修改失败");
    }

    /**
     * 通过主键删除数据
     *
     * @param id 主键
     * @return 是否成功
     */
    @Override
    public boolean deleteById(Integer id) {
        return this.materialCategoryDao.deleteById(id) > 0;
    }

    @Override
    public Result deleteMaterialCategory(Integer[] ids) {
        int one = this.materialCategoryDao.deleteMaterialCategory(ids);
        if (one == ids.length) {
            return new Result("1001", "删除成功");
        }
        return new Result("2001", "删除失败");
    }

    @Override
    public Result queryCategoryAll() {
        return new Result("1001", "查询成功", this.materialCategoryDao.queryCategoryAll());
    }
}