package com.steel.service.impl;

import com.alibaba.druid.util.StringUtils;
import com.steel.common.Result;
import com.steel.common.TokenUtil;
import com.steel.common.UuidTools;
import com.steel.dao.MaterialStockDao;
import com.steel.dao.QuotePriceDao;
import com.steel.dao.SiteInformationDao;
import com.steel.entity.*;
import com.steel.dao.PurchasePlanDao;
import com.steel.service.PurchasePlanService;
import com.sun.tools.internal.ws.wsdl.document.soap.SOAPUse;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.*;

/**
 * (PurchasePlan)表服务实现类
 *
 * @author makejava
 * @since 2020-04-02 11:18:19
 */
@Service("purchasePlanService")
public class PurchasePlanServiceImpl implements PurchasePlanService {
    @Resource
    private PurchasePlanDao purchasePlanDao;

    @Resource
    private QuotePriceDao quotePriceDao;

    @Resource
    private SiteInformationDao siteInformationDao;

    @Resource
    private MaterialStockDao materialStockDao;

    @Override
    public Result queryById(Integer id) {
        PurchasePlan purchasePlan = this.purchasePlanDao.queryById(id);
        if (purchasePlan.getDealMoney() == null) {
            purchasePlan.setDealMoney(purchasePlan.getMaterialPrice()*purchasePlan.getPurchaseQuantity());
        } else {
            purchasePlan.setMaterialPrice(purchasePlan.getDealMoney()/purchasePlan.getPurchaseQuantity());
        }
        return new Result("1001", "查询成功", purchasePlan);
    }

    @Override
    public Result queryPurchasePlanWithLimit(String token, String purchaseNumber, Integer state, String startTime, String endTime, Integer pageIndex, Integer pageSize) {
        String myId = "";
        if (!StringUtils.isEmpty(token)) {
            myId = TokenUtil.getInfoFromToken(token, "id");
        }
        return new Result("1001", "查询成功", this.purchasePlanDao.queryDataWithLimit(myId, purchaseNumber, state, startTime, endTime, pageIndex, pageSize), purchasePlanDao.queryDataCountWithLimit(myId, purchaseNumber, state, startTime, endTime));
    }

    @Override
    public Result approvalPurchasePlan(String token, Integer purchasePlanId, Integer state, String approvalNote, Double dealMoney) {
        Integer oldState = this.purchasePlanDao.queryPurchasePlanById(purchasePlanId).getState();
        if (oldState != 0) {
            return new Result("2001", "该状态不可操作");
        }
        String approvalUserId = TokenUtil.getInfoFromToken(token, "id");
        if (this.purchasePlanDao.approvalPurchasePlan(approvalUserId, purchasePlanId, state, approvalNote, dealMoney) == 1) {
            return new Result("1001", "操作成功");
        }
        return new Result("2001", "操作失败");
    }

    @Override
    public Result delPurchasePlan(Integer[] ids) {
        if (ids.length == this.purchasePlanDao.delPurchasePlan(ids)) {
            return new Result("1001", "删除成功");
        }
        return new Result("2001", "删除失败");
    }

    @Override
    public Result applyPurchasePlan(String token, String applyNote, Integer materialId, Integer purchaseQuantity, Integer supplierId) {
        Integer approvalUserId = Integer.parseInt(TokenUtil.getInfoFromToken(token, "id"));
        PurchasePlan purchasePlan = new PurchasePlan();
        purchasePlan.setPurchaseNumber(UuidTools.generateShortUuid()); // 采购计划编号
        purchasePlan.setApplyUserId(approvalUserId); // 申请人id
        purchasePlan.setMaterialId(materialId); //材料id
        purchasePlan.setPurchaseQuantity(purchaseQuantity); // 采购数量
        purchasePlan.setApplyNote(applyNote); // 申请理由
        purchasePlan.setSupplierId(supplierId);

        Integer minLowPurchase = this.quotePriceDao.searchMinPurchase(supplierId, materialId);

        if (minLowPurchase > purchaseQuantity) {
            return new Result("2001", "该种材料该经销商起购量为"+minLowPurchase);
        }

        if(this.purchasePlanDao.addPurchasePlan(purchasePlan) == 1) {
            return new Result("1001", "申请采购成功，请等待审核");
        }

        return new Result("2001", "申请失败");
    }

    @Override
    public Result getApprovalPlan() {
        return new Result("1001", "查询成功", this.purchasePlanDao.getApprovalPurchase());
    }

    @Override
    public Result donePurchasePlan(Integer id) {
        Integer count = this.purchasePlanDao.updateStateOver(id);
        if (count < 1) {
            return new Result("2001", "材料入库失败");
        }
        PurchasePlan purchasePlan = this.purchasePlanDao.queryPurchasePlanById(id);
        if (purchasePlan == null) {
            return new Result("2001", "材料入库失败");
        }

        if (materialStockDao.addMaterialQuantity(purchasePlan.getMaterialId(), purchasePlan.getPurchaseQuantity()) == 1) {
            return new Result("1001", "材料入库成功");
        }
        return new Result("2001", "材料入库失败");
    }

    @Override
    public Result updatePurchasePlanToHavaBuy(Integer id) {
        if (this.purchasePlanDao.updatePurchasePlanToHavaBuy(id) == 1) {
            return new Result("1001", "采购计划状态更新成功");
        }
        return new Result("2001", "采购计划状态更新失败");
    }

}