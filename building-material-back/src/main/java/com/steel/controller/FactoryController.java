package com.steel.controller;

import com.steel.common.Result;
import com.steel.common.VerifyToken;
import com.steel.entity.Factory;
import com.steel.service.FactoryService;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

/**
 * (Factory)表控制层
 *
 * @author makejava
 * @since 2020-03-31 20:43:23
 */
@RestController
@RequestMapping("/factory")
public class FactoryController {
    /**
     * 服务对象
     */
    @Resource
    private FactoryService factoryService;

    /**
     * 通过主键查询单条数据
     *
     * @param id 主键
     * @return 单条数据
     */
    @GetMapping("selectOne")
    public Factory selectOne(Integer id) {
        return this.factoryService.queryById(id);
    }

    @RequestMapping(value = "/queryFactory", method = RequestMethod.GET)
    public Result queryFactory(Factory factory, Integer pageIndex, Integer pageSize) {
        return this.factoryService.queryFactoryWithLimit(factory, (pageIndex-1)*pageSize, pageSize);
    }

    @RequestMapping(value = "/addFactory", method = RequestMethod.POST)
    public Result addFactoy(Factory factory) {
        return this.factoryService.insert(factory);
    }

    @RequestMapping(value = "/editFactory", method = RequestMethod.POST)
    public Result editFactory(Factory factory) {
        return this.factoryService.update(factory);
    }

    @RequestMapping(value = "/deleteFactory", method = RequestMethod.GET)
    public Result deleteFactory(Integer[] ids) {
        return this.factoryService.deleteById(ids);
    }

    @RequestMapping(value = "/queryFactoryAll", method = RequestMethod.GET)
    @VerifyToken
    public Result queryFactoryAll() {
        return this.factoryService.queryFactoryAll();
    }


}