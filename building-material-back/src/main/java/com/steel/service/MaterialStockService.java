package com.steel.service;

import com.steel.common.Result;
import com.steel.entity.MaterialStock;

/**
 * @ProjectName: building-material
 * @Package: com.steel.service
 * @ClassName: MaterialStockService
 * @Author: Hero
 * @Description: 材料库存service
 * @Date: 2020/4/17 10:03
 * @Version: 1.0
 */
public interface MaterialStockService {

    Result queryMaterialStock(String materialNumber, String materialName,Integer materialQuantity, Integer supplierId, Integer factoryId, Integer materialCategoryId, Integer offset, Integer limit);

    Result addMaterial(MaterialStock materialStock);

    Result editMaterial(MaterialStock materialStock);

    Result delMaterial(Integer[] ids);

    Result searchMaterialAndCategory();

    Result queryMaterialInCategory(Integer siteId);
}
