package com.steel.controller;

import com.steel.common.Result;
import com.steel.common.VerifyToken;
import com.steel.entity.PurchasePlan;
import com.steel.service.PurchasePlanService;
import io.swagger.models.auth.In;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

/**
 * (PurchasePlan)表控制层
 *
 * @author makejava
 * @since 2020-04-02 11:18:19
 */
@RestController
@RequestMapping("/purchasePlan")
public class PurchasePlanController {
    /**
     * 服务对象
     */
    @Resource
    private PurchasePlanService purchasePlanService;

    /**
     * 通过主键查询单条数据
     *
     * @param id 主键
     * @return 单条数据
     */
    @VerifyToken
    @GetMapping("/purchaseListDetail")
    public Result queryById(Integer id) {
        return this.purchasePlanService.queryById(id);
    }

    @VerifyToken
    @RequestMapping(value = "/queryPurchasePlan", method = RequestMethod.GET)
    public Result queryPurchasePlanWithLimit(String token, String purchaseNumber, Integer state, String startTime, String endTime, Integer pageIndex, Integer pageSize) {
        return this.purchasePlanService.queryPurchasePlanWithLimit(token, purchaseNumber, state, startTime, endTime, (pageIndex-1)*pageSize, pageSize);
    }

    @VerifyToken
    @RequestMapping(value = "/approvalPurchasePlan", method = RequestMethod.GET)
    public Result approvalPurchasePlan(String token, Integer purchasePlanId, Integer state, String approvalNote, Double dealMoney) {
        return this.purchasePlanService.approvalPurchasePlan(token, purchasePlanId, state, approvalNote, dealMoney);
    }

    @VerifyToken
    @GetMapping("/delPurchasePlan")
    public Result delPurchasePlan(Integer[] ids) {
        return this.purchasePlanService.delPurchasePlan(ids);
    }

    @VerifyToken
    @PostMapping("/applyPurchase")
    public Result applyPurchase(String token, String applyNote, Integer materialId, Integer purchaseQuantity, Integer supplierId) {
        return this.purchasePlanService.applyPurchasePlan(token, applyNote, materialId, purchaseQuantity, supplierId);
    }

    @VerifyToken
    @GetMapping("/getApprovalPurchase")
    public Result getApprovalPurchase() {
        return this.purchasePlanService.getApprovalPlan();
    }

    @PostMapping("/putInStock")
    @VerifyToken
    public Result putInStock(Integer purchasePlanId) {
        return this.purchasePlanService.donePurchasePlan(purchasePlanId);
    }

    @VerifyToken
    @PostMapping("/updatePurchasePlanToHavaBuy")
    public Result updatePurchasePlanToHavaBuy(Integer id) {
        return this.purchasePlanService.updatePurchasePlanToHavaBuy(id);
    }

}