package com.steel.dao;

import com.steel.entity.MaterialStock;
import com.steel.entity.PurchaseList;
import com.steel.entity.PurchasePlan;
import io.swagger.models.auth.In;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import java.util.List;

/**
 * (PurchasePlan)表数据库访问层
 *
 * @author makejava
 * @since 2020-04-02 11:18:19
 */
@Mapper
public interface PurchasePlanDao {

    PurchasePlan queryById(Integer id);

    List<PurchasePlan> queryDataWithLimit(String myId, String purchaseNumber, Integer state, String startTime, String endTime, Integer offset, Integer limit);

    Integer queryDataCountWithLimit(String myId, String purchaseNumber, Integer state, String startTime, String endTime);

    PurchasePlan queryPurchasePlanById(Integer id);

    Integer approvalPurchasePlan(String approvalUserId, Integer id, Integer state, String approvalNote, Double dealMoney);

    Integer delPurchasePlan(Integer[] ids);

    Integer addPurchasePlan(PurchasePlan purchasePlan);

    List<MaterialStock> getApprovalPurchase();

    Integer updateStateOver(Integer id);

    List<PurchasePlan> statisticTotalPurchase();

    List<PurchasePlan> statisticByUseSelect(String startDate, String endDate);

    Integer updatePurchasePlanToHavaBuy(Integer id);
}