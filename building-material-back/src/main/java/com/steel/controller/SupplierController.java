package com.steel.controller;

import com.steel.common.Result;
import com.steel.common.VerifyToken;
import com.steel.entity.Supplier;
import com.steel.service.SupplierService;
import io.swagger.models.auth.In;
import javafx.scene.chart.ValueAxis;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

/**
 * (Supplier)表控制层
 *
 * @author makejava
 * @since 2020-03-31 11:21:49
 */
@RestController
@RequestMapping(value = "/supplier")
public class SupplierController {
    /**
     * 服务对象
     */
    @Resource
    private SupplierService supplierService;

    /**
     * 通过主键查询单条数据
     *
     * @param id 主键
     * @return 单条数据
     */
    @GetMapping("/selectOne")
    public Supplier selectOne(Integer id) {
        return this.supplierService.queryById(id);
    }

    @VerifyToken
    @RequestMapping(value = "/querySupplier", method = RequestMethod.GET)
    public Result querySupplier(Supplier supplier, Integer pageIndex, Integer pageSize){
        return this.supplierService.querySupplierByLimit(supplier, pageIndex, pageSize);
    }

    @VerifyToken
    @RequestMapping(value = "/deleteSupplier", method = RequestMethod.GET)
    public Result deleteSupplier(Integer[] ids) {
        return this.supplierService.deleteById(ids);
    }

    @VerifyToken
    @RequestMapping(value = "/addSupplier", method = RequestMethod.POST)
    public Result addSupplier(Supplier supplier) {
        return this.supplierService.insert(supplier);
    }

    @VerifyToken
    @RequestMapping(value = "/editSupplier", method = RequestMethod.POST)
    public Result editSupplier(Supplier supplier) {
        return this.supplierService.update(supplier);
    }

    @VerifyToken
    @RequestMapping(value = "/querySupplierAll", method = RequestMethod.GET)
    public Result querySupplierAll() {
        return this.supplierService.querySupplierAll();
    }
}