package com.steel.service.impl;

import com.steel.common.Result;
import com.steel.common.TokenUtil;
import com.steel.dao.EvaluateDao;
import com.steel.entity.Evaluate;
import com.steel.service.EvaluateService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * @ProjectName: building-material
 * @Package: com.steel.service.impl
 * @ClassName: EvaluateServiceImpl
 * @Author: Hero
 * @Description: 评论服务实现
 * @Date: 2020/5/1 14:22
 * @Version: 1.0
 */
@Service("evaluateService")
public class EvaluateServiceImpl implements EvaluateService {

    @Resource
    private EvaluateDao evaluateDao;

    @Override
    public Result addEvaluate(Evaluate evaluate) {
        //String token = evaluate.getToken();
        evaluate.setUserId(Integer.parseInt(TokenUtil.getInfoFromToken(evaluate.getToken(), "id")));
        if (this.evaluateDao.insertEvaluate(evaluate) == 1) {
            return new Result("1001", "评论成功");
        }
        return new Result("2001", "评论失败");
    }

    @Override
    public Result queryEvaluate(Integer purchasePlanId) {
        Evaluate evaluate = this.evaluateDao.queryByPurchasePlanId(purchasePlanId);
        if (evaluate != null) {
            return new Result("1001", "查询成功", evaluate);
        }
        return new Result("2001", "查询失败");
    }

    @Override
    public Result getFactoryEvaluate(Integer factoryId, Integer factoryPoint, String factoryComment, Integer pageIndex, Integer pageSize) {
        return new Result("1001", "查询成功", evaluateDao.getFactoryEvaluate(factoryId, factoryPoint, 
			factoryComment, (pageIndex-1)*pageSize, pageSize), 
			evaluateDao.getFacotryEvaluateCount(factoryId, factoryPoint, factoryComment));
    }

    @Override
    public Result getSupplierEvaluate(Integer supplierId, Integer supplierPoint, String supplierComment, Integer pageIndex, Integer pageSize) {
        return new Result("1001", "查询成功", evaluateDao.getSupplierEvaluate(supplierId, supplierPoint, supplierComment, (pageIndex-1)*pageSize, pageSize), evaluateDao.getSupplierEvaluateCount(supplierId, supplierPoint, supplierComment));
    }

}
