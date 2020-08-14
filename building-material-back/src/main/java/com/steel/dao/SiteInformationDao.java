package com.steel.dao;

import com.steel.entity.SiteInformation;
import com.steel.entity.SiteUsage;
import io.swagger.models.auth.In;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * @ProjectName: building-material
 * @Package: com.steel.dao
 * @ClassName: SiteInformationDao
 * @Author: Hero
 * @Description: 工地信息dao
 * @Date: 2020/4/26 10:43
 * @Version: 1.0
 */
@Mapper
public interface SiteInformationDao {

    List<SiteInformation> querySiteInformation(String siteName, Integer siteState, Integer offset, Integer limit);

    Integer querySiteCount(String siteName, Integer siteState);

    Integer delSiteInformation(Integer[] ids);

    SiteInformation queryById(Integer id);

    Integer updateSiteInformation(SiteInformation siteInformation);

    Integer addSiteInformation(SiteInformation siteInformation);

    List<SiteUsage> querySiteUsageById(Integer siteId, String materialName, Double startPercent, Double endPercent, Integer offset, Integer limit);

    Integer querySiteUsageByIdCount(Integer siteId, String materialName, Double startPercent, Double endPercent);

    Integer addUsageForSite(Integer siteId, Integer materialId, Integer putQuantity);

    List<Integer> queryUsage(Integer siteId);

    Integer appendMaterial(Integer id, Integer putQuantity);

    Integer updateUseQuantity(Integer id, Integer useQuantity);

    SiteUsage queryUseById(Integer id);

    Integer queryTotalUseQuantity();
}
