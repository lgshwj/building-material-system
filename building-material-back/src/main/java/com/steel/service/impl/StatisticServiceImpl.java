package com.steel.service.impl;

import com.steel.common.MonthAndYearDealTools;
import com.steel.common.Result;
import com.steel.dao.PurchasePlanDao;
import com.steel.dao.SiteInformationDao;
import com.steel.entity.BarChartForInt;
import com.steel.entity.Barchart;
import com.steel.entity.Linechart;
import com.steel.entity.PurchasePlan;
import com.steel.service.StatisticService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * @ProjectName: building-material
 * @Package: com.steel.service.impl
 * @ClassName: StatisticServiceImpl
 * @Author: Hero
 * @Description: 统计service实现类
 * @Date: 2020/4/30 16:39
 * @Version: 1.0
 */
@Service("statisticService")
public class StatisticServiceImpl implements StatisticService {

    @Resource
    private PurchasePlanDao purchasePlanDao;

    @Resource
    private SiteInformationDao siteInformationDao;

    @Override
    public Result statisticTotalPurchase() {
        List<PurchasePlan> list = this.purchasePlanDao.statisticTotalPurchase();

        Integer purchaseTotalQuantity = 0;
        Double purchaseTotalMoney = 0.00;
        Map<String, Map<String, Double[]>> map = new HashMap<>();

        // x轴数据：年份
        TreeSet<String> xSet = new TreeSet<>();
        for (PurchasePlan purchasePlan : list) {
            // 存放在xSet中
            xSet.add(purchasePlan.getDoneYear());
            // 采购总数
            purchaseTotalQuantity += purchasePlan.getPurchaseQuantity();
            purchaseTotalMoney += purchasePlan.getDealMoney();
            // map中没该经销商
            if (!map.containsKey(purchasePlan.getSupplierName())) {
                // 创建一个子map为value,key为经销商
                Map<String, Double[]> sonMap = new LinkedHashMap<>();
                // 子map中执行年份为key, 采购量为value[0]中，value[1]金额
                Double[] value = {(double)purchasePlan.getPurchaseQuantity(), purchasePlan.getDealMoney()};
                sonMap.put(purchasePlan.getDoneYear(), value);
                map.put(purchasePlan.getSupplierName(), sonMap);
            } else {
                Map<String, Double[]> sonMap = map.get(purchasePlan.getSupplierName());
                if (sonMap.containsKey(purchasePlan.getDoneYear())) {
                    Double[] oldValue = sonMap.get(purchasePlan.getDoneYear());
                    Double[] value = {oldValue[0]+purchasePlan.getPurchaseQuantity(), oldValue[1]+purchasePlan.getDealMoney()};
                    sonMap.put(purchasePlan.getDoneYear(), value);
                } else {
                    Double[] value = {(double)purchasePlan.getPurchaseQuantity(), purchasePlan.getDealMoney()};
                    sonMap.put(purchasePlan.getDoneYear(), value);
                }
                map.put(purchasePlan.getSupplierName(), sonMap);
            }
        }

        List<String> legendList = new ArrayList<>();
        List<Barchart> barSeriesList = new ArrayList<>();
        List<Linechart> lineSeriesList = new ArrayList<>();
        for(String keyName : map.keySet()) {
            legendList.add(keyName);
            Map<String, Double[]> dataMap = map.get(keyName);
            ArrayList<Double> dataQuantity = new ArrayList<>();
            ArrayList<Double> dataMoney = new ArrayList<>();
            /*for (String doneYear : dataMap.keySet()) {
                Double[] temp = dataMap.get(doneYear);
                dataQuantity.add(temp[0]);
                dataMoney.add(temp[1]/10000);
            }*/
            for (String year: xSet) {
                if (!dataMap.containsKey(year)) {
                    dataQuantity.add(0.00);
                    dataMoney.add(null);
                } else {
                    dataQuantity.add(dataMap.get(year)[0]);
                    dataMoney.add(dataMap.get(year)[1]/10000);
                }
            }
            Barchart barchart = new Barchart(keyName, dataQuantity);
            Linechart linechart = new Linechart(keyName, dataMoney);
            barSeriesList.add(barchart);
            lineSeriesList.add(linechart);
        }

        Map<String, Object> reMap = new HashMap<>();
        reMap.put("xAxisList", xSet); //x轴：年份
        reMap.put("barSeriesList", barSeriesList);// 柱形图数据
        reMap.put("lineSeriesList", lineSeriesList);// 折线图数据
        reMap.put("legendList", legendList); // 图例
        reMap.put("purchaseTotalQuantity", purchaseTotalQuantity); // 采购总量
        Integer useTotalQuantity = siteInformationDao.queryTotalUseQuantity();
        reMap.put("useTotalQuantity", useTotalQuantity == null ? 0:useTotalQuantity);// 消耗总数量
        reMap.put("purchaseTotalMoney", String.format("%.2f", purchaseTotalMoney/1000)); //采购总金额
        return new Result("1001", "查询成功", reMap);
    }

    @Override
    public Result statisticBySelect(Integer type, String startDate, String endDate) throws Exception {
        if (type == null || startDate == null || startDate.equals("") || endDate == null || endDate.equals("")) {
            return new Result("2001", "统计方式或时间范围不能为空");
        }

        ArrayList<String> xAxisList = new ArrayList<>();
        List<PurchasePlan> list = null;
        List<BarChartForInt> barChartCategorySeries = null;
        List<BarChartForInt> barChartSupplierSeries = null;
        List<String> legendCategory = null;
        List<String> legendSupplier = null;
        Map<String, Object> mapCategory = null;
        Map<String, Object> mapSupplier = null;
        if (type == 1) {//按月统计
            endDate = MonthAndYearDealTools.monthAddOne(endDate);
            list = purchasePlanDao.statisticByUseSelect(startDate+"-01", endDate+"-01");

            xAxisList = MonthAndYearDealTools.monthRangeFromStartDateToEndDate(startDate, endDate);// x轴数据

            mapCategory =  dealDataBySelect(list, startDate, endDate,1,2, xAxisList);// 按材料类别处理后的数据

            mapSupplier = dealDataBySelect(list, startDate, endDate, 2,2, xAxisList);// 按经销商处理后的数据

        } else {// 按年统计
            startDate = MonthAndYearDealTools.yearAndMonthToYear(startDate);
            endDate = (Integer.parseInt(MonthAndYearDealTools.yearAndMonthToYear(endDate))+1)+"";
            list = purchasePlanDao.statisticByUseSelect(startDate+"-01-01", endDate+"-01-01");
            for (Integer i = Integer.parseInt(startDate); i<Integer.parseInt(endDate); i++) {
                xAxisList.add(i+"");
            }
            mapCategory =  dealDataBySelect(list, startDate, endDate,1,1, xAxisList);// 按材料类别处理后的数据
            mapSupplier = dealDataBySelect(list, startDate, endDate, 2,1, xAxisList);// 按经销商处理后的数据

        }

        barChartCategorySeries = (List<BarChartForInt>)mapCategory.get("series");
        legendCategory = (List<String>) mapCategory.get("legend");

        barChartSupplierSeries = (List<BarChartForInt>)mapSupplier.get("series");
        legendSupplier = (List<String>) mapSupplier.get("legend");

        Map<String, Object> reMap = new HashMap<>();
        reMap.put("xAxisList", xAxisList);// x轴数据
        reMap.put("legendCategory", legendCategory); // 按类别统计数据legend
        reMap.put("barChartCategorySeries", barChartCategorySeries);// 按类别统计数据series
        reMap.put("legendSupplier", legendSupplier);// 按经销商统计数据legend
        reMap.put("barChartSupplierSeries", barChartSupplierSeries);// 按经销商统计数据series

        if (xAxisList.size() == 1) {
            return new Result("4001", "建议将时间范围扩大", reMap);
        } else if (xAxisList.size()>36){
            return new Result("4001", "建议将时间范围缩小", reMap);
        }
        return new Result("1001", "数据统计成功", reMap);
    }


    public Map<String, Object> dealDataBySelect(List<PurchasePlan> list, String startDate, String endDate, Integer way, Integer type, List<String> legendX) {
        Map<String, Map<String, Integer>> map = new HashMap<>();
        for (PurchasePlan purchasePlan : list) {
            String keyName = "";
            if (way == 1) { //按类别
                keyName = purchasePlan.getMaterialCategoryName();
            } else { // 按经销商
                keyName = purchasePlan.getSupplierName();
            }
            if (!map.containsKey(keyName)) {
                Map<String, Integer> sonMap = new HashMap<>();
                String sonMapKey = "";
                if (type == 1) {// 年
                    sonMapKey = purchasePlan.getDoneYear();
                } else { // 月
                    if (purchasePlan.getDoneMonth().length()==1) {
                        sonMapKey = purchasePlan.getDoneYear()+"-0"+purchasePlan.getDoneMonth();
                    } else {
                        sonMapKey = purchasePlan.getDoneYear()+"-"+purchasePlan.getDoneMonth();
                    }
                }
                sonMap.put(sonMapKey, purchasePlan.getPurchaseQuantity());
                map.put(keyName, sonMap);
            } else {
                Map<String, Integer> sonMap = map.get(keyName);
                String sonMapKey = "";
                if (type == 2) {// 月
                    if (purchasePlan.getDoneMonth().length()==1) {
                        sonMapKey = purchasePlan.getDoneYear()+"-0"+purchasePlan.getDoneMonth();
                    } else {
                        sonMapKey = purchasePlan.getDoneYear()+"-"+purchasePlan.getDoneMonth();
                    }
                } else { // 年
                    sonMapKey = purchasePlan.getDoneYear();
                }
                if (sonMap.containsKey(sonMapKey)) {
                    sonMap.put(sonMapKey, purchasePlan.getPurchaseQuantity()+sonMap.get(sonMapKey));
                } else {
                    sonMap.put(sonMapKey, purchasePlan.getPurchaseQuantity());
                }
                map.put(keyName, sonMap);
            }
        }

        List<BarChartForInt> barChartForIntList = new ArrayList<>();
        List<String> legendList = new ArrayList<>();
        for (String keyName : map.keySet()) {
            legendList.add(keyName);
            Map<String, Integer> sonMap = map.get(keyName);
            ArrayList<Integer> data = new ArrayList<>();
            if (type == 1) { // 年
                for (Integer i = Integer.parseInt(startDate); i<Integer.parseInt(endDate); i++) {
                    if (!sonMap.containsKey(i+"")) {
                        data.add(0);
                    } else {
                        data.add(sonMap.get(i+""));
                    }
                }
            } else { // 月
                for (Integer i = 0 ; i<legendX.size() ; i++) {
                    if (!sonMap.containsKey(legendX.get(i))) {
                        data.add(0);
                    } else {
                        data.add(sonMap.get(legendX.get(i)));
                    }
                }

            }

            BarChartForInt barChartForInt = new BarChartForInt(keyName, data);
            barChartForIntList.add(barChartForInt);
        }
        Map<String, Object> reMap = new HashMap<>();
        reMap.put("series", barChartForIntList);
        reMap.put("legend", legendList);
        return reMap;
    }
}
