package com.steel.service;

import com.steel.common.Result;
import com.steel.entity.SiteInformation;

/**
 * @ProjectName: building-material
 * @Package: com.steel.service
 * @ClassName: SiteInformationService
 * @Author: Hero
 * @Description: 工地信息service
 * @Date: 2020/4/26 14:01
 * @Version: 1.0
 */
public interface SiteInformationService {

    Result querySiteInformation(String siteName, Integer siteState, Integer pageIndex, Integer pageSize);

    Result getSiteInformationById(Integer id);

    Result delSiteInformation(Integer[] ids);

    Result updateSiteInformaiton(SiteInformation siteInformation);

    Result addSiteInfomation(SiteInformation siteInformation);

    Result addUsage(Integer siteId, Integer materialId, Integer putQuantity);

    Result querySiteMaterialUsage(Integer siteId, String materialName, Integer type, Integer pageIndex, Integer pageSize);

    Result appendUsage(Integer id, Integer materialId, Integer putQuantity);

    Result updateUseQuantity(Integer id, Integer useQuantity);
}
