package com.steel.service.impl;

import cn.afterturn.easypoi.cache.manager.IFileLoader;
import com.steel.common.Result;
import com.steel.dao.MaterialStockDao;
import com.steel.dao.SiteInformationDao;
import com.steel.entity.SiteInformation;
import com.steel.entity.SiteUsage;
import com.steel.service.SiteInformationService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.List;

/**
 * @ProjectName: building-material
 * @Package: com.steel.service.impl
 * @ClassName: SiteInformationServiceImpl
 * @Author: Hero
 * @Description: 工地信息service实现
 * @Date: 2020/4/26 14:03
 * @Version: 1.0
 */
@Service("siteInformationService")
public class SiteInformationServiceImpl implements SiteInformationService {

    @Resource
    private SiteInformationDao siteInformationDao;

    @Resource
    private MaterialStockDao materialStockDao;

    @Override
    public Result querySiteInformation(String siteName, Integer siteState, Integer pageIndex, Integer pageSize) {
        return new Result("1001", "查询成功", siteInformationDao.querySiteInformation(siteName, siteState, (pageIndex-1)*pageSize, pageSize), siteInformationDao.querySiteCount(siteName, siteState));
    }

    @Override
    public Result getSiteInformationById(Integer id) {
        return new Result("1001", "查询成功", siteInformationDao.queryById(id));
    }

    @Override
    public Result delSiteInformation(Integer[] ids) {
        if (ids.length == siteInformationDao.delSiteInformation(ids)) {
            return new Result("1001", "删除成功");
        }
        return new Result("2001", "删除失败");
    }

    @Override
    public Result updateSiteInformaiton(SiteInformation siteInformation) {
        if (siteInformationDao.updateSiteInformation(siteInformation) == 1) {
            return new Result("1001", "更新成功");
        }
        return new Result("2001", "更新失败");
    }

    @Override
    public Result addSiteInfomation(SiteInformation siteInformation) {
        if (siteInformationDao.addSiteInformation(siteInformation) == 1) {
            return new Result("1001", "新增成功");
        }
        return new Result("2001", "新增失败");
    }

    @Override
    public Result addUsage(Integer siteId, Integer materialId, Integer putQuantity) {
        Integer stockQuantity = materialStockDao.queryQuantityById(materialId);
        if (putQuantity > stockQuantity) {
            return new Result("2001", "该材料库存中只有"+stockQuantity);
        }

        if (siteInformationDao.addUsageForSite(siteId, materialId, putQuantity) == 1) {
            if (materialStockDao.addMaterialQuantity(materialId, 0-putQuantity) == 1) {
                return new Result("1001", "投入材料成功");
            }
        }
        return new Result("2001", "投入材料失败");
    }

    @Override
    public Result querySiteMaterialUsage(Integer siteId, String materialName, Integer type, Integer pageIndex, Integer pageSize) {

        Double endPercent = 2.25, startPercent = 2.25;
        if (type != null) {
            if (type == 1) {
                startPercent = 0.00;
                endPercent = 0.50;
            } else if (type == 2){
                startPercent = 0.50;
                endPercent = 0.80;
            } else if (type == 3) {
                startPercent = 0.80;
                endPercent = 1.00;
            } else {

            }
        }

        List<SiteUsage> list = siteInformationDao.querySiteUsageById(siteId, materialName, startPercent, endPercent, (pageIndex-1)*pageSize, pageSize);
        Integer total = siteInformationDao.querySiteUsageByIdCount(siteId, materialName, startPercent, endPercent);
        return new Result("1001", "查询成功", list, total);
    }

    @Override
    @Transactional
    public Result appendUsage(Integer id, Integer materialId, Integer putQuantity) {
        if (putQuantity < 1) {
            return new Result("2001", "追加材料数量不能小于1");
        }
        Integer stockQuantity = materialStockDao.queryQuantityById(materialId);
        if (putQuantity > stockQuantity) {
            return new Result("2001", "该材料库存余量不足");
        }

        if (siteInformationDao.appendMaterial(id, putQuantity) == 1) {
            if (materialStockDao.addMaterialQuantity(materialId, 0-putQuantity) == 1) {
                return new Result("1001", "追加材料成功");
            }
        }

        return new Result("2001", "追加材料失败");
    }

    @Override
    public Result updateUseQuantity(Integer id, Integer useQuantity) {

        if (useQuantity > this.siteInformationDao.queryUseById(id).getMaterialTotalQuantity()) {
            return new Result("2001", "该材料消耗数量不能大于材料总量");
        }

        if (siteInformationDao.updateUseQuantity(id, useQuantity) == 1) {
            return new Result("1001", "更新材料消耗量成功");
        }
        return new Result("2001", "更新材料消耗量失败");
    }
}
