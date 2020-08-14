package com.steel.entity;

import lombok.Getter;
import lombok.Setter;

import java.util.Date;
import java.io.Serializable;

/**
 * (PurchasePlan)实体类
 *
 * @author makejava
 * @since 2020-04-02 11:18:19
 */
@Getter
@Setter
public class PurchasePlan implements Serializable {
    private static final long serialVersionUID = 576482587951014616L;
    /**
    * id主键
    */
    private Integer id;
    /**
    * 采购编号
    */
    private String purchaseNumber;
    /**
    * 申请人id
    */
    private Integer applyUserId;

    /**
    * 申请人username
    * */
    private String applyUsername;

    /**
    * 申请日期
    */
    private Date applyDate;
    /**
    * 状态0未审核，1已驳回，2已批准，3已执行
    */
    private Integer state;
    /**
    * 审批时间
    */
    private Date approvalDate;
    /**
    * 审批人id
    */
    private Integer approvalUserId;

    private String approvalUsername;

    /**
    * 0代表未删，1代表已删
    */
    private Integer isDelete;
    /**
    * 审核备注：驳回时给予理由
    */
    private String approvalNote;

    // 申请理由
    private String applyNote;

    // 材料id
    private Integer materialId;

    // 采购数量
    private Integer purchaseQuantity;

    // 材料编号
    private String materialNumber;

    // 材料名称
    private String materialName;

    // 规格
    private String norm;

    // 材质
    private String materialQuality;

    // 材料单位
    private String materialUnit;

    // 供应商id
    private Integer supplierId;

    // 供应商名称
    private String supplierName;

    // 厂家id
    private Integer factoryId;

    // 厂家名称
    private String factoryName;

    // 材料价格
    private Double materialPrice;

    private String doneDate;

    private String doneYear;

    private String doneMonth;

    private Double dealMoney;

    private Integer materialCategoryId;

    private String materialCategoryName;
}