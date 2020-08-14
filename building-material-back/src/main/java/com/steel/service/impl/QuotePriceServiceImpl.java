package com.steel.service.impl;

import com.steel.common.ExcleAnalysisTool;
import com.steel.common.MonthAndYearDealTools;
import com.steel.common.Result;
import com.steel.dao.FactoryDao;
import com.steel.dao.SupplierDao;
import com.steel.entity.*;
import com.steel.dao.QuotePriceDao;
import com.steel.service.QuotePriceService;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import java.util.*;

/**
 * (QuotePrice)表服务实现类
 *
 * @author makejava
 * @since 2020-04-19 18:07:56
 */
@Service("quotePriceService")
public class QuotePriceServiceImpl implements QuotePriceService {
    @Resource
    private QuotePriceDao quotePriceDao;

    @Resource
    private SupplierDao supplierDao;

    @Resource
    private FactoryDao factoryDao;

    /**
     * 通过主键删除数据
     *
     * @param ids 主键
     * @return 是否成功
     */
    @Override
    public Result deleteById(Integer[] ids) {
        if (this.quotePriceDao.deleteById(ids) == ids.length) {
            return new Result("1001", "删除成功");
        }
        return new Result("2001", "删除失败");
    }

    @Override
    public Result queryQuotePrice(String norm, String materialQuality, String factoryName, Integer supplierId, String materialName, String releaseStartTime, String releaseEndTime, Integer state, Integer pageIndex, Integer pageSize) {
        List<QuotePrice> list = this.quotePriceDao.queryQuotePrice(norm, materialQuality, factoryName, supplierId, materialName, releaseStartTime, releaseEndTime, state, (pageIndex-1)*pageSize, pageSize);
        Integer count = this.quotePriceDao.queryQuotePriceCount(norm, materialQuality, factoryName, supplierId, materialName, releaseStartTime, releaseEndTime, state);
        return new Result("1001", "查询成功", list, count);
    }

    @Override
    public Result uploadQuotePrice(MultipartFile uploadFile) {
        try {
            if (uploadFile == null) {
                return new Result("2001", "文件不存在");
            }
            List<Supplier> supplierList = supplierDao.querySupplierAll();
            List<Factory> factoryList = factoryDao.queryFactoryAll();
            ExcleAnalysisTool execleAnalysisTool = new ExcleAnalysisTool();
            Map<String, Object> map = execleAnalysisTool.readExecleFile(uploadFile, supplierList, factoryList);

            if (map.containsKey("error")) {
                return new Result("2001", (String) map.get("error"));
            }
            List<QuotePrice> list = (List<QuotePrice>)map.get("list");
            if (list == null) {
                return new Result("2001", "数据为空");
            }

            this.quotePriceDao.insertBatch(list);

            return new Result("1001", "导入成功");
        } catch (Exception e) {
            e.printStackTrace();
            return new Result("2001", "导入失败");
        }
    }

    @Override
    public Result searchSupplierByMaterialId(Integer materialId) {
        return new Result("1001", "查询成功", this.quotePriceDao.searchSupplierByMaterialId(materialId));
    }

    @Override
    public Result analysisQuotePrice(Integer materialId, String startDate, String endDate) throws Exception {
        endDate = MonthAndYearDealTools.monthAddOne(endDate);

        // 在startDate-endDate之间的一种（materialId）材料的所有供应商报价信息
        List<QuotePrice> list = this.quotePriceDao.analysisQuotePrice(materialId, startDate, endDate);

        // 返回x轴所需数据 例如：{"2019-12", "2020-01", "2020-02"}
        ArrayList<String> xAxisList = MonthAndYearDealTools.monthRangeFromStartDateToEndDate(startDate, endDate);

        Map<String, Map<String, Double>> map = new HashMap<>();
        for (QuotePrice quotePrice : list) {
            String supplierName = quotePrice.getSupplierName();
            if (!map.containsKey(supplierName)) {
                Map<String, Double> sonMap = new HashMap<>();
                sonMap.put(quotePrice.getReleaseDate(), quotePrice.getMaterialPrice());
                map.put(supplierName, sonMap);
            } else {
                Map<String, Double> sonMap = map.get(supplierName);
                String sonKey = quotePrice.getReleaseDate();
                sonMap.put(sonKey, quotePrice.getMaterialPrice());
                map.put(supplierName, sonMap);
            }
        }

        List<Linechart> seriesList = new ArrayList<>();
        List<String> legendList = new ArrayList<>();
        for (String supplierName : map.keySet()) {
            legendList.add(supplierName);
            //Linechart linechart = new Linechart(supplierName, map.get(supplierName));

            Map<String, Double> sonMap = map.get(supplierName);
            ArrayList<Double> data = new ArrayList<>();
            for (int i=0 ; i<xAxisList.size() ; i++) {
                if (!sonMap.containsKey(xAxisList.get(i))) {
                    data.add(null);
                } else {
                    data.add(sonMap.get(xAxisList.get(i)));
                }
            }
            Linechart linechart = new Linechart(supplierName, data);
            seriesList.add(linechart);
        }

        Map<String, Object> reMap = new HashMap<>();
        reMap.put("xAxisList", xAxisList);
        reMap.put("seriesList", seriesList);
        reMap.put("legendList", legendList);
        return new Result("1001", "查询成功", reMap);
    }
}