package com.steel.entity;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

/**
 * @ProjectName: building-material
 * @Package: com.steel.entity
 * @ClassName: MaterialStock
 * @Author: Hero
 * @Description: 材料库存
 * @Date: 2020/4/17 9:37
 * @Version: 1.0
 */
@Getter
@Setter
public class MaterialStock {

    private Integer id;

    private String materialNumber;

    private String materialName;

    private Integer supplierId;

    private String supplierName;

    private Integer factoryId;

    private String factoryName;

    private Integer materialQuantity;

    private String materialUnit;

    private Integer materialLow;

    private Integer materialHigh;

    private Integer materialCategoryId;

    private String materialCategoryName;

    private Integer isDelete;

    private String norm;

    private String materialQuality;

    private List<MaterialStock> child;
}
