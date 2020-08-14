package com.steel.entity;

import io.swagger.models.auth.In;
import lombok.Getter;
import lombok.Setter;

/**
 * @ProjectName: building-material
 * @Package: com.steel.entity
 * @ClassName: Evaluate
 * @Author: Hero
 * @Description: 评价实体
 * @Date: 2020/5/1 14:12
 * @Version: 1.0
 */
@Getter
@Setter
public class Evaluate {
    private Integer id;

    private Integer supplierId;

    private String supplierComment;

    private Integer factoryId;

    private String factoryComment;

    private Integer supplierPoint;

    private Integer factoryPoint;

    private Integer isDelete;

    private Integer purchasePlanId;

    private Integer userId;

    private String token;

    private String evaluteTime;

    private String username;
}
