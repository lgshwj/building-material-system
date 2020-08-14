package com.steel.entity;

import io.swagger.models.auth.In;
import lombok.Getter;
import lombok.Setter;

/**
 * @ProjectName: building-material
 * @Package: com.steel.entity
 * @ClassName: SiteUsage
 * @Author: Hero
 * @Description: 工地用料详情
 * @Date: 2020/4/29 17:28
 * @Version: 1.0
 */
@Getter
@Setter
public class SiteUsage {

    private Integer id;

    private Integer siteId; //工地id

    private Integer materialId;//材料id

    private String materialName;//材料名称

    private String norm;//规格

    private String materialQuality;// 材质

    private String materialUnit;//单位

    private Integer materialUseQuantity;//材料使用量

    private Integer materialTotalQuantity;//材料总量

    private Integer maxAddQuantity;//最大能投多少材料量（投入量须不能大于库存中此种材料存量）
}
