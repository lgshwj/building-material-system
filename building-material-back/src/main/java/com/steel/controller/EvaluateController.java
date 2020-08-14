package com.steel.controller;

import com.steel.common.Result;
import com.steel.common.VerifyToken;
import com.steel.entity.Evaluate;
import com.steel.service.EvaluateService;
import jdk.nashorn.internal.objects.annotations.Getter;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

/**
 * @ProjectName: building-material
 * @Package: com.steel.controller
 * @ClassName: EvaluateController
 * @Author: Hero
 * @Description: 评论controller
 * @Date: 2020/5/1 14:25
 * @Version: 1.0
 */
@RestController
@RequestMapping("/evaluate")
public class EvaluateController {

    @Resource
    private EvaluateService evaluateService;

    @VerifyToken
    @PostMapping("/addEvaluate")
    public Result addEvaluate(Evaluate evaluate) {
        return this.evaluateService.addEvaluate(evaluate);
    }

    @VerifyToken
    @GetMapping("/queryEvaluateByPurchasePlanId")
    public Result queryEvaluateByPurchasePlanId(Integer purchasePlanId) {
        return this.evaluateService.queryEvaluate(purchasePlanId);
    }

    @VerifyToken
    @GetMapping("/getFactoryEvaluate")
    public Result getEvaluateFactory(Integer factoryId, Integer factoryPoint, String factoryComment, Integer pageIndex, Integer pageSize) {
        return this.evaluateService.getFactoryEvaluate(factoryId, factoryPoint, factoryComment, pageIndex, pageSize);
    }

    @VerifyToken
    @GetMapping("/getSupplierEvaluate")
    public Result getEvaluateSupplier(Integer supplierId, Integer supplierPoint, String supplierComment, Integer pageIndex, Integer pageSize) {
        return this.evaluateService.getSupplierEvaluate(supplierId, supplierPoint, supplierComment, pageIndex, pageSize);
    }
}
