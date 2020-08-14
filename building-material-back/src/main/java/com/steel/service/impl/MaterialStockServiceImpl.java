package com.steel.service.impl;

import com.steel.common.Result;
import com.steel.common.UuidTools;
import com.steel.dao.MaterialStockDao;
import com.steel.dao.SiteInformationDao;
import com.steel.entity.MaterialStock;
import com.steel.entity.SelectOptions;
import com.steel.service.MaterialStockService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @ProjectName: building-material
 * @Package: com.steel.service.impl
 * @ClassName: MaterialStockServiceImpl
 * @Author: Hero
 * @Description: 材料库存服务实现
 * @Date: 2020/4/17 10:03
 * @Version: 1.0
 */
@Service("materialStockService")
public class MaterialStockServiceImpl implements MaterialStockService {

    @Autowired
    private MaterialStockDao materialStockDao;

    @Resource
    private SiteInformationDao siteInformationDao;

    @Override
    public Result queryMaterialStock(String materialNumber, String materialName,Integer materialQuantity, Integer supplierId, Integer factoryId, Integer materialCategoryId, Integer offset, Integer limit) {
        List<MaterialStock> list = materialStockDao.queryMateial(materialNumber, materialName, supplierId, factoryId, materialCategoryId,(offset-1)*limit, limit);
        if (materialQuantity == null) {
            Integer count = materialStockDao.queryMaterialCount(materialNumber, materialName, supplierId, factoryId, materialCategoryId);
            return new Result("1001", "查询成功", list, count);
        } else {
            List<MaterialStock> aList1 = new ArrayList<>();
            List<MaterialStock> aList2 = new ArrayList<>();
            List<MaterialStock> aList3 = new ArrayList<>();
            for (MaterialStock materialStock : list) {
                if (materialStock.getMaterialQuantity() < materialStock.getMaterialLow()) {
                    aList1.add(materialStock);
                } else if(materialStock.getMaterialQuantity() > materialStock.getMaterialHigh()) {
                    aList3.add(materialStock);
                } else {
                    aList2.add(materialStock);
                }
            }

            if (materialQuantity == 1) {
                return new Result("1001", "查询成功", aList1, aList1.size());
            } else if (materialQuantity == 2) {
                return new Result("1001", "查询成功", aList2, aList2.size());
            } else {
                return new Result("1001", "查询成功", aList3, aList3.size());
            }

        }


    }

    @Override
    public Result addMaterial(MaterialStock materialStock) {
        materialStock.setMaterialNumber(UuidTools.generateShortUuid());
        if (materialStockDao.checkMaterialDup(materialStock.getMaterialName(), materialStock.getNorm(), materialStock.getMaterialQuality(), materialStock.getId()) == 0) {
            if (materialStockDao.insertMaterial(materialStock) == 1) {
                return new Result("1001", "新增成功");
            }
            return new Result("2001", "新增失败");
        }
        return new Result("2001", "该种材料已存在");
    }

    @Override
    public Result editMaterial(MaterialStock materialStock) {
        if (this.materialStockDao.checkMaterialDup(materialStock.getMaterialName(), materialStock.getNorm(), materialStock.getMaterialQuality(), materialStock.getId()) != 0) {
            return new Result("2001", "该种材料已存在");
        } else {
            if (this.materialStockDao.updateMaterial(materialStock) == 1) {
                return new Result("1001", "修改成功");
            }
            return new Result("2001", "修改失败");
        }

    }

    @Override
    public Result delMaterial(Integer[] ids) {
        if (this.materialStockDao.delMaterial(ids) == ids.length) {
            return new Result("1001", "删除成功");
        }
        return new Result("2001", "删除失败");
    }

    @Override
    public Result searchMaterialAndCategory() {
        List<MaterialStock> list = this.materialStockDao.searchMaterialAndCategoryInQuotePrice();
        return new Result("1001", "查询成功", dealSearchData(list));
    }

    @Override
    public Result queryMaterialInCategory(Integer siteId) {
        List<Integer> list = this.siteInformationDao.queryUsage(siteId);
        return new Result("1001", "查询成功", dealSearchData(materialStockDao.queryMaterialInCategoryNotIncludeUsageMaterial(list)));
    }


    public List<SelectOptions> dealSearchData(List<MaterialStock> list) {
        List<SelectOptions> reList = new ArrayList<>();
        Map<Integer, String> map = new HashMap<>();
        for (MaterialStock materialStock : list) {
            if (!map.containsKey(materialStock.getMaterialCategoryId())) { // 不包含，直接加
                map.put(materialStock.getMaterialCategoryId(), "material_category_id");
                SelectOptions selectOptions = new SelectOptions(materialStock.getMaterialCategoryId(), materialStock.getMaterialCategoryName());
                reList.add(selectOptions);
            }
        }
        for (SelectOptions selectOptions : reList) {
            List<SelectOptions> childList = new ArrayList<>();
            for (MaterialStock materialStock : list) {
                if (materialStock.getMaterialCategoryId() == selectOptions.getValue()) {
                    SelectOptions cSelectOptions = new SelectOptions(materialStock.getId(), materialStock.getMaterialName()+"-"+materialStock.getNorm()+"-"+materialStock.getMaterialQuality());
                    childList.add(cSelectOptions);
                }
            }
            selectOptions.setChildren(childList);
        }
        return reList;
    }
}
