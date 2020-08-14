package com.steel.entity;

import lombok.Getter;
import lombok.Setter;

/**
 * @ProjectName: building-material
 * @Package: com.steel.entity
 * @ClassName: PurchaseList
 * @Author: Hero
 * @Description: 采购清单实体
 * @Date: 2020/4/2 15:22
 * @Version: 1.0
 */
@Getter
@Setter
public class PurchaseList {
    private Integer id;

    private String purchaseMaterialName;

    private Integer purchasePlanId;

    private Integer purchaseQuantity;

    private String purchaseUnit;

    private Integer supplierId;

    private String supplierName;

    private Double purchasePrice;
}
