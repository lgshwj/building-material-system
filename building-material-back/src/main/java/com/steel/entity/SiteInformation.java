package com.steel.entity;

import lombok.Getter;
import lombok.Setter;

/**
 * @ProjectName: building-material
 * @Package: com.steel.entity
 * @ClassName: SiteInformation
 * @Author: Hero
 * @Description: 工地信息实体
 * @Date: 2020/4/26 10:38
 * @Version: 1.0
 */
@Getter
@Setter
public class SiteInformation {
    private Integer id;

    private String siteName;

    private String siteAddress;

    private String siteContact;

    private String contactPhone;

    private Integer siteState;

    private String startDate;

    private String expectedEndDate;

    private String factEndDate;

    private Integer isDelete;
}
