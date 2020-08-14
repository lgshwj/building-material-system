package com.steel.controller;

import com.steel.common.Result;
import com.steel.common.VerifyToken;
import com.steel.entity.MaterialCategory;
import com.steel.service.MaterialCategoryService;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

/**
 * (MaterialCategory)表控制层
 *
 * @author makejava
 * @since 2020-03-13 17:01:27
 */
@RestController
@RequestMapping("materialCategory")
public class MaterialCategoryController {
    /**
     * 服务对象
     */
    @Resource
    private MaterialCategoryService materialCategoryService;

    @VerifyToken
    @RequestMapping(value = "/queryMaterialCategoryWithLimit", method = RequestMethod.GET)
    public Result queryMaterialWithLimit(String materialCategoryName, Integer pageIndex, Integer pageSize) {
        return this.materialCategoryService.queryAllByLimit(materialCategoryName, (pageIndex-1)*pageSize, pageSize);
    }

    @VerifyToken
    @RequestMapping(value = "/addMaterialCategory", method = RequestMethod.POST)
    public Result addMateialCategory(MaterialCategory materialCategory) {
        return this.materialCategoryService.insert(materialCategory);
    }

    @VerifyToken
    @RequestMapping(value = "/deleteMaterialCategory", method = RequestMethod.GET)
    public Result deleteMaterialCategory(Integer[] ids) {
        return this.materialCategoryService.deleteMaterialCategory(ids);
    }

    @VerifyToken
    @RequestMapping(value = "/updateMaterialCategory", method = RequestMethod.POST)
    public Result updateMaterialCategory(MaterialCategory materialCategory) {
        return this.materialCategoryService.update(materialCategory);
    }

    @VerifyToken
    @RequestMapping(value = "/queryCategroyAll", method = RequestMethod.GET)
    public Result queryCategoryAll() {
        return this.materialCategoryService.queryCategoryAll();
    }
}