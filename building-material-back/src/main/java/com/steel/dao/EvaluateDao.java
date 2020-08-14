package com.steel.dao;

import com.steel.entity.Evaluate;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * @ProjectName: building-material
 * @Package: com.steel.dao
 * @ClassName: EvaluateDao
 * @Author: Hero
 * @Description: 评论dao
 * @Date: 2020/5/1 14:14
 * @Version: 1.0
 */
@Mapper
public interface EvaluateDao {

    Integer insertEvaluate(Evaluate evaluate);

    Evaluate queryByPurchasePlanId(Integer purchasePlanId);

    List<Evaluate> getFactoryEvaluate(Integer factoryId, Integer factoryPoint, String factoryComment, Integer offset, Integer limit);

    List<Evaluate> getSupplierEvaluate(Integer supplierId, Integer supplierPoint, String supplierComment, Integer offset, Integer limit);

    Integer getFacotryEvaluateCount(Integer factoryId, Integer factoryPoint, String factoryComment);

    Integer getSupplierEvaluateCount(Integer supplierId, Integer supplierPoint, String supplierComment);
}
