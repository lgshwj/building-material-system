package com.steel.entity;

import lombok.Getter;
import lombok.Setter;

import java.util.Date;
import java.io.Serializable;

/**
 * (QuotePrice)实体类
 *
 * @author makejava
 * @since 2020-04-19 18:07:56
 */
@Getter
@Setter
public class QuotePrice implements Serializable {

    private static final long serialVersionUID = 529413402626527376L;

    private Integer id;

    private Integer materialId; // 材料id

    private String materialNumber; // 材料编号

    private String materialName; // 材料名称

    private String norm; // 规格

    private String materialQuality; // 材质

    private String materialUnit; // 单位

    private Integer supplierId; // 供应商id

    private String supplierNumber; // 经销商编号

    private String supplierName; // 经销商名称

    private  Integer factoryId;// 厂家id

    private String factoryNumber;// 厂家编号

    private String factoryName; // 厂家名称

    private Integer materialQuantity; // 起订量

    private Double materialPrice; // 报价（单价）

    private String manufactureDate; // 生产日期

    private String qualityGuaranteePeriod; // 保质期

    private String releaseDate; // 发布日期

    private String originPlace; // 产地

    private Integer state; // 状态

    private Integer isDelete;

    private String nowDate;

}