package com.steel.service.impl;

import com.steel.common.Result;
import com.steel.common.UuidTools;
import com.steel.entity.Supplier;
import com.steel.dao.SupplierDao;
import com.steel.service.SupplierService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
 * (Supplier)表服务实现类
 *
 * @author makejava
 * @since 2020-03-31 11:21:49
 */
@Service("supplierService")
public class SupplierServiceImpl implements SupplierService {
    @Resource
    private SupplierDao supplierDao;

    @Override
    public Supplier queryById(Integer id) {
        return this.supplierDao.queryById(id);
    }

    @Override
    public List<Supplier> queryAllByLimit(int offset, int limit) {
        return this.supplierDao.queryAllByLimit(offset, limit);
    }

    @Override
    public Result insert(Supplier supplier) {
        supplier.setSupplierNumber(UuidTools.generateShortUuid());
        if(this.supplierDao.insert(supplier) == 1) {
            return new Result("1001", "新增成功");
        }
        return new Result("2001", "新增失败");
    }

    @Override
    public Result update(Supplier supplier) {
        if (this.supplierDao.update(supplier) == 1) {
            return new Result("1001", "修改成功");
        }
        return new Result("2001", "修改失败");
    }

    @Override
    public Result deleteById(Integer[] ids) {
        int temp = this.supplierDao.deleteSupplier(ids);
        if (temp == ids.length) {
            return new Result("1001", "删除成功");
        }
        return new Result("2001", "删除失败");
    }

    @Override
    public Result querySupplierByLimit(Supplier supplier, Integer pageIndex, Integer pageSize) {
        List<Supplier> list = this.supplierDao.querySupplierByLimit(supplier, (pageIndex-1)*pageSize, pageSize);
        Integer count = this.supplierDao.querySupplierByLimitCount(supplier);
        return new Result("1001", "查询成功", list, count);
    }

    @Override
    public Result querySupplierAll() {
        return new Result("1001", "查询成功", this.supplierDao.querySupplierAll());
    }
}