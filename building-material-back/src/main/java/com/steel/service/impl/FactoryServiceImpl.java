package com.steel.service.impl;

import com.steel.common.Result;
import com.steel.common.UuidTools;
import com.steel.dao.EvaluateDao;
import com.steel.entity.Factory;
import com.steel.dao.FactoryDao;
import com.steel.service.FactoryService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
 * (Factory)表服务实现类
 *
 * @author makejava
 * @since 2020-03-31 20:43:23
 */
@Service("factoryService")
public class FactoryServiceImpl implements FactoryService {
    @Resource
    private FactoryDao factoryDao;


    /**
     * 通过ID查询单条数据
     *
     * @param id 主键
     * @return 实例对象
     */
    @Override
    public Factory queryById(Integer id) {
        return this.factoryDao.queryById(id);
    }

    /**
     * 查询多条数据
     *
     * @param offset 查询起始位置
     * @param limit 查询条数
     * @return 对象列表
     */
    @Override
    public List<Factory> queryAllByLimit(int offset, int limit) {
        return this.factoryDao.queryAllByLimit(offset, limit);
    }

    /**
     * 新增数据
     *
     * @param factory 实例对象
     * @return 实例对象
     */
    @Override
    public Result insert(Factory factory) {
        factory.setFactoryNumber(UuidTools.generateShortUuid());
        if(this.factoryDao.insert(factory) == 1) {
            return new Result("1001", "新增成功");
        }
        return new Result("2001", "新增失败");
    }

    /**
     * 修改数据
     *
     * @param factory 实例对象
     * @return 实例对象
     */
    @Override
    public Result update(Factory factory) {
        if(this.factoryDao.update(factory) == 1) {
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
        return this.factoryDao.deleteById(id) > 0;
    }

    @Override
    public Result<Factory> queryFactoryWithLimit(Factory factory, Integer pageIndex, Integer pageSize) {
        return new Result("1001", "查询成功", this.factoryDao.queryFactoryWihtLimit(factory, pageIndex, pageSize), this.factoryDao.queryFactoryCountWihtLimit(factory));
    }

    @Override
    public Result deleteById(Integer[] ids) {
        if (this.factoryDao.deleteFactory(ids) == ids.length) {
            return new Result("1001", "删除成功");
        }
        return new Result("2001", "删除失败");
    }

    @Override
    public Result queryFactoryAll() {
        return new Result("1001", "查询成功", this.factoryDao.queryFactoryAll());
    }


}