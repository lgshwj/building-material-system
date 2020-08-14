package com.steel.service.impl;

import com.steel.common.Result;
import com.steel.dao.NavigationDao;
import com.steel.entity.Navigation;
import com.steel.service.NavigationService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
 * (TbNavigation)表服务实现类
 *
 * @author makejava
 * @since 2020-03-09 16:34:14
 */
@Service("tbNavigationService")
public class NavigationServiceImpl implements NavigationService {
    @Resource
    private NavigationDao navigationDao;

    /**
     * 通过ID查询单条数据
     *
     * @param id 主键
     * @return 实例对象
     */
    @Override
    public Navigation queryById(Integer id) {
        return this.navigationDao.queryById(id);
    }

    /**
     * 查询多条数据
     *
     * @param offset 查询起始位置
     * @param limit 查询条数
     * @return 对象列表
     */
    @Override
    public List<Navigation> queryAllByLimit(String navigationName, int offset, int limit) {
        return this.navigationDao.queryAllByLimit(navigationName, offset, limit);
    }

    /**
     * 新增数据
     *
     * @param tbNavigation 实例对象
     * @return 实例对象
     */
    @Override
    public Result insert(Navigation tbNavigation) {
        int one = this.navigationDao.duplicateForNavigationRoute(tbNavigation.getNavigationRoute(), null);
        if (one > 0) {
            return new Result("2001", "导航路由已存在，请重新输入");
        }
        int i = this.navigationDao.insert(tbNavigation);
        if (i == 1) {
            return new Result("1001", "新增成功");
        } else {
            return new Result("2001", "新增失败");
        }
    }

    /**
     * 修改数据
     *
     * @param tbNavigation 实例对象
     * @return 实例对象
     */
    @Override
    public Result update(Navigation tbNavigation) {
        int one = this.navigationDao.duplicateForNavigationRoute(tbNavigation.getNavigationRoute(), tbNavigation.getId());
        if (one > 0) {
            return new Result("2001", "导航路由已存在，请重新输入");
        }
        int answer = this.navigationDao.update(tbNavigation);
        if (answer > 0) {
            return new Result("1001", "修改成功");
        } else {
            return new Result("2001", "修改失败，请重新尝试");
        }
    }

    /**
     * 通过主键删除数据
     *
     * @param id 主键
     * @return 是否成功
     */
    @Override
    public boolean deleteById(Integer id) {
        return this.navigationDao.deleteById(id) > 0;
    }

    @Override
    public Integer queryByLimitCount(String navigationName) {
        return this.navigationDao.queryByLimitCount(navigationName);
    }

    @Override
    public Result deleteNavigation(Integer[] ids) {
        if (this.navigationDao.deleteNavigation(ids) == ids.length) {
            return new Result("1001", "删除成功");
        } else {
            return new Result("2001", "删除失败");
        }

    }

    @Override
    public List<Navigation> queryAllNavigation() {
        return this.navigationDao.queryAll();
    }
}