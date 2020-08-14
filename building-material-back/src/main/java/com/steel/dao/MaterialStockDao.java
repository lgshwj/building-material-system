package com.steel.dao;

import com.steel.entity.MaterialStock;
import io.swagger.models.auth.In;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * @ProjectName: building-material
 * @Package: com.steel.dao
 * @ClassName: MaterialStockDao
 * @Author: Hero
 * @Description: 材料库存dao
 * @Date: 2020/4/17 9:42
 * @Version: 1.0
 */
@Mapper
public interface MaterialStockDao {

    List<MaterialStock> queryMateial(String materialNumber, String materialName, Integer supplierId, Integer factoryId, Integer materialCategoryId, Integer offset, Integer limit);

    Integer queryMaterialCount(String materialNumber, String materialName, Integer supplierId, Integer factoryId, Integer materialCategoryId);

    Integer insertMaterial(MaterialStock materialStock);

    Integer checkMaterialDup(String materialName,String norm, String materialQuality, Integer id);

    Integer updateMaterial(MaterialStock materialStock);

    Integer delMaterial(Integer[] ids);

    Integer checkMaterial(String materialNumber, String materialName, String norm, String materialQuality);

    Integer searchIdByNumber(String materialNumber);

    Integer addMaterialQuantity(Integer id, Integer materialQuantity);

    List<MaterialStock> searchMaterialAndCategoryInQuotePrice();

    Integer queryQuantityById(Integer id);

    List<MaterialStock> queryMaterialInCategoryNotIncludeUsageMaterial(List<Integer> list);

}
