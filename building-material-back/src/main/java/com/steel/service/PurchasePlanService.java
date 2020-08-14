package com.steel.service;

import com.steel.common.Result;
import com.steel.entity.PurchasePlan;
import java.util.List;

/**
 * (PurchasePlan)表服务接口
 *
 * @author makejava
 * @since 2020-04-02 11:18:19
 */
public interface PurchasePlanService {

    Result queryById(Integer id);

    Result queryPurchasePlanWithLimit(String token, String purchaseNumber, Integer state, String startTime, String endTime, Integer pageIndex, Integer pageSize);

    Result approvalPurchasePlan(String token, Integer purchasePlanId, Integer state, String approvalNote, Double dealMoney);

    Result delPurchasePlan(Integer[] ids);

    Result applyPurchasePlan(String token, String applyNote, Integer materialId, Integer purchaseQuantity, Integer supplierId);

    Result getApprovalPlan();

    Result donePurchasePlan(Integer id);

    Result updatePurchasePlanToHavaBuy(Integer id);
}