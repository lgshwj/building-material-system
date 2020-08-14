package com.steel.service;

import com.steel.common.Result;
import com.steel.entity.Evaluate;
import io.swagger.models.auth.In;

/**
 * @ProjectName: building-material
 * @Package: com.steel.service
 * @ClassName: EvaluateService
 * @Author: Hero
 * @Description: 评论服务
 * @Date: 2020/5/1 14:21
 * @Version: 1.0
 */
public interface EvaluateService {

    Result addEvaluate(Evaluate evaluate);

    Result queryEvaluate(Integer purchasePlanId);

    Result getFactoryEvaluate(Integer factoryId, Integer factoryPoint, String factoryComment, Integer pageIndex, Integer pageSize);

    Result getSupplierEvaluate(Integer supplierId, Integer supplierPoint, String supplierComment, Integer pageIndex, Integer pageSize);
}
